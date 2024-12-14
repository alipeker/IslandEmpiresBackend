package main

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"sync"
	"time"

	"github.com/segmentio/kafka-go"
)

func main() {
	clanIdList := []int64{1, 2, 3, 1023, 1025, 1026, 1033, 1053}
	clanIdList2 := []int64{}
	for j := 0; j < 100; j++ {
		clanIdList2 = append(clanIdList2, clanIdList...)
	}

	// Create a Kafka writer
	writer := kafka.NewWriter(kafka.WriterConfig{
		Brokers:      []string{"localhost:9092"},
		Topic:        "clan-change-subject",
		Balancer:     &kafka.Hash{},
		BatchSize:    100,                   // Batch multiple messages
		BatchTimeout: 10 * time.Millisecond, // Small timeout for batching
	})

	defer writer.Close()

	// Send messages concurrently
	sendMessagesConcurrently(writer, clanIdList2)

	fmt.Println("All messages sent to Kafka successfully")
}

func sendMessagesConcurrently(writer *kafka.Writer, clanIds []int64) {
	const workerCount = 10                        // Number of concurrent workers
	messageChan := make(chan int64, len(clanIds)) // Channel for clan IDs

	// Start worker goroutines
	var wg sync.WaitGroup
	for i := 0; i < workerCount; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			for clanId := range messageChan {
				sendMessage(writer, clanId)
			}
		}()
	}

	// Send clan IDs to the channel
	for _, clanId := range clanIds {
		messageChan <- clanId
	}
	close(messageChan)

	// Wait for all workers to finish
	wg.Wait()
}

func sendMessage(writer *kafka.Writer, clanId int64) {
	// Convert clanId to JSON
	clanJson, err := json.Marshal(clanId)
	if err != nil {
		log.Println("Error marshalling clanId to JSON:", err)
		return
	}

	// Send message to Kafka
	err = writer.WriteMessages(context.TODO(), kafka.Message{
		Value: clanJson,
	})
	if err != nil {
		log.Println("Error sending message to Kafka:", err)
	} else {
		fmt.Println("Message sent successfully:", string(clanJson))
	}
}
