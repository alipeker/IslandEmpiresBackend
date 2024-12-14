package main

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"strconv"
	"sync"

	"github.com/elastic/go-elasticsearch/v8"
	"github.com/segmentio/kafka-go"
)

var (
	kafkaReaderList []*kafka.Reader
	topicList       = []string{
		"clan-delete-topic", "clan-change-topic", "clan-user-change-topic", "island-population-change-topic"}
)

func initiliazeKafkaWriter(client *GatewayClient, esClient *elasticsearch.Client) {
	initKafka()

	var wg sync.WaitGroup

	for _, kafkaReader := range kafkaReaderList {
		wg.Add(1)
		go func(reader *kafka.Reader) {
			defer wg.Done()
			listenKafka(reader, client, esClient)
			reader.Close() // Explicitly close the reader after processing
		}(kafkaReader)
	}

	wg.Wait()
}

func createTopics() {
	for _, topic := range topicList {
		conn, err := kafka.DialLeader(context.Background(), "tcp", "localhost:9092", topic, 0)
		if err != nil {
			log.Println("Error dialing Kafka:", err)
			continue
		}
		conn.Close()
	}

}

func initKafka() {
	for _, topic := range topicList {
		kafkaReaderinit := kafka.NewReader(kafka.ReaderConfig{
			Brokers: []string{"localhost:9092"},
			Topic:   topic,
			GroupID: "map-write-service",
		})

		kafkaReaderList = append(kafkaReaderList, kafkaReaderinit)
	}
}

func listenKafka(kafkaReader *kafka.Reader, client *GatewayClient, esClient *elasticsearch.Client) {
	for {
		message, err := kafkaReader.ReadMessage(context.Background())
		if err != nil {
			log.Println("Error reading message from Kafka:", err)
			return
		}
		fmt.Printf("Message from topic %s: %s\n", kafkaReader.Config().Topic, string(message.Value))

		// Process messages
		processMessage(message, kafkaReader.Config().Topic, client, esClient)
	}
}

func processMessage(message kafka.Message, topic string, client *GatewayClient, esClient *elasticsearch.Client) {
	switch topic {
	case "clan-delete-topic":
		go processClanDelete(message, client, esClient)
	case "clan-change-topic":
		go processClanSpecsUpdate(message, client, esClient)
	case "clan-user-change-topic":
		go processClanUserUpdate(message, client, esClient)
	case "island-population-change-topic":
		go processIslandPopulationChange(message, client, esClient)
	default:
		log.Println("Unknown topic:", topic)
	}
}

func byteSliceToInt64(value []byte) (int64, error) {
	strValue := string(value)

	result, err := strconv.ParseInt(strValue, 10, 64)
	if err != nil {
		return 0, fmt.Errorf("failed to convert value to int64: %w", err)
	}

	return result, nil
}

func processClanSpecsUpdate(message kafka.Message, client *GatewayClient, esClient *elasticsearch.Client) {
	clanId, err := byteSliceToInt64(message.Value)
	if err != nil {
		log.Println("Error converting message value to int64:", err)
		return
	}

	updatedClanInfo, err := client.GetClan(clanId)
	if err != nil {
		log.Println("Error getting updated clan info from Gateway:", err)
		return
	}

	// update clans index in Elasticsearch with the new clan specs
	err = updateClanById(esClient, clanId, updatedClanInfo)
	if err != nil {
		log.Println("Error updating clan data to Elasticsearch:", err)
	}

}

func processClanUserUpdate(message kafka.Message, client *GatewayClient, esClient *elasticsearch.Client) {
	var kafkaClanUser KafkaClanUser
	err := json.Unmarshal(message.Value, &kafkaClanUser)
	if err != nil {
		log.Println("Error unmarshalling message value to KafkaClanUser:", err)
		return
	}
	if err != nil {
		log.Println("Error converting message value to int64:", err)
		return
	}

	updatedClanInfo, err := client.GetClanUser(kafkaClanUser.UserId, kafkaClanUser.ServerId)
	if err != nil {
		log.Println("Error getting updated clan info from Gateway:", err)
		return
	}

	fmt.Println(updatedClanInfo)

	err = updateClanUserByServerIdAndUserId(esClient, kafkaClanUser.ServerId, kafkaClanUser.UserId, updatedClanInfo)
	if err != nil {
		log.Println("Error updating clan data to Elasticsearch:", err)
	}

}

func processClanDelete(message kafka.Message, client *GatewayClient, esClient *elasticsearch.Client) {
	clanId, err := byteSliceToInt64(message.Value)
	if err != nil {
		log.Println("Error converting message value to int64:", err)
		return
	}

	err = deleteClanById(esClient, clanId)
	if err != nil {
		log.Println("Error deleting clan data from Elasticsearch:", err)
	}
}

func processIslandPopulationChange(message kafka.Message, client *GatewayClient, esClient *elasticsearch.Client) {
	islandId := string(message.Value)

	islandResource, err := client.GetIslandResource(islandId)
	if err != nil {
		log.Println("Error getting updated island info from Gateway:", err)
		return
	}

	err = updatePopulationbyIslandID(esClient, islandId, islandResource.Population)
	if err != nil {
		log.Println("Error updating island data to Elasticsearch:", err)
	}

}
