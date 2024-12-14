package main

import (
	"context"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"sync"

	"github.com/segmentio/kafka-go"
	"go.mongodb.org/mongo-driver/bson"
)

var (
	kafkaReader *kafka.Reader
	kafkaWriter *kafka.Writer
)

func main() {
	createTopic("clan-stream")
	initKafka()
	defer kafkaReader.Close()
	defer kafkaWriter.Close()

	var wg sync.WaitGroup
	numWorkers := 5 // Number of concurrent workers
	wg.Add(numWorkers)
	for i := 0; i < numWorkers; i++ {
		go listenKafka(&wg)
	}

	wg.Wait()
}

func initKafka() {
	kafkaReader = kafka.NewReader(kafka.ReaderConfig{
		Brokers: []string{"localhost:9092"},
		Topic:   "clan-change-topic",
		GroupID: "clan-stream-group",
	})

	kafkaWriter = kafka.NewWriter(kafka.WriterConfig{
		Brokers: []string{"localhost:9092"},
		Topic:   "clan-stream",
	})
}

// listen kafka topic method
func listenKafka(wg *sync.WaitGroup) {
	defer wg.Done()

	for {
		message, err := kafkaReader.ReadMessage(context.Background())
		if err != nil {
			log.Println("Error reading message from Kafka:", err)
			return
		}

		go processMessage(message)
	}
}

func processMessage(message kafka.Message) {
	var clanId int64
	err := bson.UnmarshalExtJSON(message.Value, false, &clanId)
	if err != nil {
		log.Println("Error unmarshalling message from Kafka:", err)
		return
	}

	fmt.Println("Message received from Kafka:", clanId)

	clan, err := getClan(clanId)
	if err == nil {
		clanMessage := map[string]interface{}{
			"id":   clanId,
			"data": clan,
		}
		sendToKafka(clanMessage)
	}
}

func getClan(clanId int64) (interface{}, error) {
	url := fmt.Sprintf("http://localhost:9001/clan/private/getClan/%d", clanId)

	req, err := http.NewRequest("GET", url, nil)
	if err != nil {
		return nil, err
	}

	req.Header.Set("Content-Type", "application/json")

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return nil, err
	}

	var result interface{}
	err = json.Unmarshal(body, &result)
	if err != nil {
		return nil, err
	}

	return result, nil
}

func sendToKafka(clan interface{}) {
	message, err := bson.MarshalExtJSON(clan, false, false)
	if err != nil {
		log.Println("Error marshalling resource to BSON JSON:", err)
		return
	}

	err = kafkaWriter.WriteMessages(context.TODO(), kafka.Message{
		Value: message,
	})
	if err != nil {
		log.Println("Error sending message to Kafka:", err)
		return
	}

	fmt.Println("Message sent to Kafka successfully:", string(message))
}

func createTopic(topic string) {
	conn, err := kafka.DialLeader(context.Background(), "tcp", "localhost:9092", topic, 0)
	if err != nil {
		log.Println("Error dialing Kafka:", err)
		return
	}
	conn.Close()
}
