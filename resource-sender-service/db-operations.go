package main

import (
	"context"
	"fmt"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
)

func fetchUserSessionData(client *mongo.Client) ([]UserSession, error) {
	collection := client.Database("session").Collection("Session")
	cursor, err := collection.Find(context.TODO(), bson.M{})
	if err != nil {
		fmt.Println("Error fetching data:", err)
		return nil, err
	}
	defer cursor.Close(context.TODO())

	var userSession []UserSession
	if err = cursor.All(context.TODO(), &userSession); err != nil {
		fmt.Println("Error decoding data:", err)
		return nil, err
	}

	return userSession, err
}

func fetchResourcesByUserSessions(ctx context.Context, client *mongo.Client, databaseName, collectionName string, userSessions []UserSession) ([]IslandResource, error) {
	collection := client.Database(databaseName).Collection(collectionName)

	// Build the $or filter
	var filters []bson.M
	for _, session := range userSessions {
		filters = append(filters, bson.M{"userId": session.UserID, "serverId": session.ServerID})
	}
	filter := bson.M{"$and": filters}

	// Execute the query
	cursor, err := collection.Find(ctx, filter)
	if err != nil {
		return nil, err
	}
	defer cursor.Close(ctx)

	// Decode the results into a slice of IslandResource
	var resources []IslandResource
	for cursor.Next(ctx) {
		var resource IslandResource
		if err := cursor.Decode(&resource); err != nil {
			return nil, err
		}
		resources = append(resources, resource)
	}

	// Check for any cursor errors
	if err := cursor.Err(); err != nil {
		return nil, err
	}

	return resources, nil
}
