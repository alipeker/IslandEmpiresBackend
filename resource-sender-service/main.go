package main

import (
	"context"
	"fmt"
	"log"
	"time"

	"github.com/segmentio/kafka-go"
	"go.mongodb.org/mongo-driver/bson"
)

func main() {
	ticker := time.NewTicker(5 * time.Second)
	defer ticker.Stop()

	for range ticker.C {
		runJob()
	}

}

func runJob() {
	userSessionClient, err := connectUserSessionMongo()
	if err != nil {
		return
	}

	userSessionList, err := fetchUserSessionData(userSessionClient)
	if err != nil {
		return
	}

	if len(userSessionList) == 0 {
		return
	}

	client, err := connectMongo()
	if err != nil {
		return
	}

	islandResourceList, err := fetchResourcesByUserSessions(context.Background(), client, "resources", "IslandResource", userSessionList)
	if err != nil {
		return
	}

	if len(islandResourceList) == 0 {
		return
	}

	resourceMap := make(map[string]UserSession)
	for _, session := range userSessionList {
		resourceMap[session.ID] = UserSession{
			ID:             session.ID,
			UserID:         session.UserID,
			JwtToken:       session.JwtToken,
			ServerID:       session.ServerID,
			IslandResource: []IslandResource{},
		}
	}

	for _, islandResource := range islandResourceList {
		for _, session := range userSessionList {
			if islandResource.UserID == session.UserID && islandResource.ServerId == session.ServerID {
				resource := resourceMap[session.ID]
				resource.IslandResource = append(resource.IslandResource, islandResource)
				resourceMap[session.ID] = resource
			}
		}
	}

	for _, resource := range resourceMap {
		sendToKafka(resource)
	}
}

func sendToKafka(resource UserSession) {
	// Create Kafka writer
	writer := kafka.NewWriter(kafka.WriterConfig{
		Brokers: []string{"localhost:9092"},
		Topic:   "island-resource-stream",
	})

	// Serialize struct to JSON using BSON tags
	message, err := bson.MarshalExtJSON(resource, false, false)
	if err != nil {
		log.Println("Error marshalling resource to BSON JSON:", err)
		return
	}

	// Send message to Kafka
	err = writer.WriteMessages(context.TODO(), kafka.Message{
		Value: message,
	})
	if err != nil {
		log.Println("Error sending message to Kafka:", err)
		return
	}

	fmt.Println("Message sent to Kafka successfully:", string(message))
	writer.Close()
}

func createTopic(topic string) {
	conn, err := kafka.DialLeader(context.Background(), "tcp", "localhost:9092", topic, 0)
	if err != nil {
		log.Println("Error dialing Kafka:", err)
		return
	}
	conn.Close()
}
