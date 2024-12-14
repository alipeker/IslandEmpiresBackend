package main

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"strings"

	"github.com/elastic/go-elasticsearch/v8"
)

func main() {
	gatewayClient := NewGatewayClient("http://localhost:9001")

	cfg := elasticsearch.Config{
		Addresses: []string{
			"http://localhost:9201", // Change the host and port as needed
		},
		Username: "elastic",              // Add your Elasticsearch username here
		Password: "xo5NB+mgtR9DFQWa9wd_", // Add your Elasticsearch password here
	}

	// Create the client
	esClient, err := elasticsearch.NewClient(cfg)
	if err != nil {
		log.Fatalf("Error creating Elasticsearch client: %v", err)
	}

	initiliazeKafkaWriter(gatewayClient, esClient)
}

func main2() {
	gatewayClient := NewGatewayClient("http://localhost:9001")

	cfg := elasticsearch.Config{
		Addresses: []string{
			"http://localhost:9201", // Change the host and port as needed
		},
		Username: "elastic",              // Add your Elasticsearch username here
		Password: "xo5NB+mgtR9DFQWa9wd_", // Add your Elasticsearch password here
	}

	// Create the client
	esClient, err := elasticsearch.NewClient(cfg)
	if err != nil {
		log.Fatalf("Error creating Elasticsearch client: %v", err)
	}

	deleteAll(esClient)

	if err := saveAllIslandsToElasticsearch(gatewayClient, esClient); err != nil {
		log.Fatalf("Error: %v", err)
	}
}

func deleteAll(esClient *elasticsearch.Client) {
	deleteAllUsersFromElasticsearch(esClient)
	deleteAllIslandsFromElasticsearch(esClient)
	deleteAllClansFromElasticsearch(esClient)
}

func deleteAllUsersFromElasticsearch(esClient *elasticsearch.Client) error {
	query := `{
		"query": {
			"match_all": {}
		}
	}`

	res, err := esClient.DeleteByQuery(
		[]string{"users"},
		strings.NewReader(query),
		esClient.DeleteByQuery.WithContext(context.Background()),
	)

	if err != nil {
		return err
	}
	defer res.Body.Close()

	log.Printf("DeleteByQuery response: %v", res)

	if res.IsError() {
		return fmt.Errorf("error deleting documents: %s", res.String())
	}

	return nil
}

func deleteAllClansFromElasticsearch(esClient *elasticsearch.Client) error {
	query := `{
		"query": {
			"match_all": {}
		}
	}`

	res, err := esClient.DeleteByQuery(
		[]string{"clans"},
		strings.NewReader(query),
		esClient.DeleteByQuery.WithContext(context.Background()),
	)

	if err != nil {
		return err
	}
	defer res.Body.Close()

	log.Printf("DeleteByQuery response: %v", res)

	if res.IsError() {
		return fmt.Errorf("error deleting documents: %s", res.String())
	}

	return nil
}

func deleteAllIslandsFromElasticsearch(esClient *elasticsearch.Client) error {
	query := `{
		"query": {
			"match_all": {}
		}
	}`

	res, err := esClient.DeleteByQuery(
		[]string{"islands"},
		strings.NewReader(query),
		esClient.DeleteByQuery.WithContext(context.Background()),
	)

	if err != nil {
		return err
	}
	defer res.Body.Close()

	log.Printf("DeleteByQuery response: %v", res)

	if res.IsError() {
		return fmt.Errorf("error deleting documents: %s", res.String())
	}

	return nil
}

func saveAllIslandsToElasticsearch(client *GatewayClient, esClient *elasticsearch.Client) error {

	islands, err := client.GetIslands()
	if err != nil {
		return err
	}

	resources, err := client.GetIslandResources()
	if err != nil {
		return err
	}

	userClans, err := client.GetUserClans()
	if err != nil {
		return err
	}

	combinedIslands := []IslandCombined{}

	for _, island := range islands {
		var resource IslandResourceDTO
		for _, res := range resources {
			if res.IslandID == island.ID {
				resource = res
				break
			}
		}

		combined := IslandCombined{
			ID:         island.ID,
			ServerID:   island.ServerID,
			Name:       island.Name,
			UserID:     island.UserID,
			X:          island.X,
			Y:          island.Y,
			Population: resource.Population,
		}

		combinedIslands = append(combinedIslands, combined)
	}

	clans, err := client.GetClans()
	if err != nil {
		return err
	}

	saveUserClansToElastic(esClient, userClans)
	saveIslandDataToElastic(esClient, combinedIslands)
	saveClansDataToElastic(esClient, clans)
	return nil
}

func saveUserClansToElastic(esClient *elasticsearch.Client, userClans []PublicClanUser) error {
	for _, userClan := range userClans {
		saveUserClanToElastic(esClient, userClan)
	}
	return nil
}

func saveUserClanToElastic(esClient *elasticsearch.Client, userClan PublicClanUser) error {
	body, err := json.Marshal(userClan)
	if err != nil {
		log.Printf("Failed to marshal island: %v", err)
		return err
	}
	esResp, err := esClient.Index("users", bytes.NewReader(body))
	if err != nil {
		log.Printf("Failed to index island: %v", err)
		return err
	}
	bodyBytes, _ := io.ReadAll(esResp.Body)
	log.Printf("Successfully indexed island: %v, response body: %s", esResp, bodyBytes)

	return nil
}

func saveClansDataToElastic(esClient *elasticsearch.Client, clans []PublicClan) error {
	for _, clan := range clans {
		saveClanDataToElastic(esClient, clan)
	}
	return nil
}

func saveClanDataToElastic(esClient *elasticsearch.Client, clan PublicClan) error {
	body, err := json.Marshal(clan)
	if err != nil {
		log.Printf("Failed to marshal island: %v", err)
		return err
	}

	log.Printf("Size of island JSON: %d bytes", len(body))

	esResp, err := esClient.Index("clans", bytes.NewReader(body))
	if err != nil {
		log.Printf("Failed to index island: %v", err)
		return err
	}

	bodyBytes, _ := io.ReadAll(esResp.Body)
	log.Printf("Successfully indexed island: %v, response body: %s", esResp, bodyBytes)

	return nil
}

func saveIslandDataToElastic(esClient *elasticsearch.Client, combinedIslands []IslandCombined) error {
	for _, island := range combinedIslands {

		body, err := json.Marshal(island)
		if err != nil {
			log.Printf("Failed to marshal island: %v", err)
			continue
		}

		log.Printf("Size of island JSON: %d bytes", len(body))

		esResp, err := esClient.Index("islands", bytes.NewReader(body))
		if err != nil {
			log.Printf("Failed to index island: %v", err)
		} else {
			bodyBytes, _ := io.ReadAll(esResp.Body)
			log.Printf("Successfully indexed island: %v, response body: %s", esResp, bodyBytes)
		}
	}
	return nil
}

// get clan with id in elastic
func getClanFromElastic(esClient *elasticsearch.Client, clanID string) (PublicClan, error) {
	query := `{
		"query": {
			"match": {
				"id": "` + clanID + `"
			}
		}
	}`

	res, err := esClient.Search(
		esClient.Search.WithContext(context.Background()),
		esClient.Search.WithIndex("clans"),
		esClient.Search.WithBody(strings.NewReader(query)),
		esClient.Search.WithTrackTotalHits(true),
		esClient.Search.WithPretty(),
	)

	if err != nil {
		return PublicClan{}, err
	}
	defer res.Body.Close()

	if res.IsError() {
		return PublicClan{}, fmt.Errorf("error searching for clan: %s", res.String())
	}

	var r map[string]interface{}
	if err := json.NewDecoder(res.Body).Decode(&r); err != nil {
		return PublicClan{}, err
	}

	log.Printf("Search results: %v", r)

	return PublicClan{}, nil
}

func updateClanById(es *elasticsearch.Client, clanId int64, updatedClan PublicClan) error {
	// Convert the updated clan to JSON
	clanJSON, err := json.Marshal(updatedClan)
	if err != nil {
		return fmt.Errorf("Error marshaling updated clan: %s", err)
	}

	// Update the document by its ID
	res, err := es.Index(
		"clans", // Replace with your clans index name
		bytes.NewReader(clanJSON),
		es.Index.WithDocumentID(fmt.Sprintf("%d", clanId)),
		es.Index.WithRefresh("true"), // Refresh the index to make the update visible immediately
	)
	if err != nil {
		return fmt.Errorf("Error updating document: %s", err)
	}
	defer res.Body.Close()

	if res.IsError() {
		return fmt.Errorf("Error response from Elasticsearch: %s", res.String())
	}

	return nil
}

func updateClanUserByServerIdAndUserId(es *elasticsearch.Client, serverId string, userId int64, updatedClanUser PublicClanUser) error {
	// Convert the updated clan user to JSON
	clanUserJSON, err := json.Marshal(updatedClanUser)
	if err != nil {
		return fmt.Errorf("Error marshaling updated clan user: %s", err)
	}

	// Search for the document by userId and serverId
	query := fmt.Sprintf(`{
        "query": {
            "bool": {
                "must": [
                    { "term": { "userId": %d } },
                    { "term": { "serverId": "%s" } }
                ]
            }
        }
    }`, userId, serverId)

	searchRes, err := es.Search(
		es.Search.WithIndex("users"),
		es.Search.WithBody(strings.NewReader(query)),
		es.Search.WithContext(context.Background()),
	)
	if err != nil {
		return fmt.Errorf("Error searching for document: %s", err)
	}
	defer searchRes.Body.Close()

	if searchRes.IsError() {
		return fmt.Errorf("Error response from Elasticsearch: %s", searchRes.String())
	}

	var searchResult map[string]interface{}
	if err := json.NewDecoder(searchRes.Body).Decode(&searchResult); err != nil {
		return fmt.Errorf("Error parsing search response: %s", err)
	}

	hits := searchResult["hits"].(map[string]interface{})["hits"].([]interface{})
	if len(hits) == 0 {
		return fmt.Errorf("Document not found for userId: %d and serverId: %s", userId, serverId)
	}

	// Get the document ID
	docID := hits[0].(map[string]interface{})["_id"].(string)

	// Update the document by its ID
	updateRes, err := es.Index(
		"users", // Replace with your users index name
		bytes.NewReader(clanUserJSON),
		es.Index.WithDocumentID(docID),
		es.Index.WithRefresh("true"), // Refresh the index to make the update visible immediately
	)
	if err != nil {
		return fmt.Errorf("Error updating document: %s", err)
	}
	defer updateRes.Body.Close()

	if updateRes.IsError() {
		return fmt.Errorf("Error response from Elasticsearch: %s", updateRes.String())
	}

	log.Printf("Successfully updated user: %d on server: %s", userId, serverId)
	return nil
}

func deleteServerUserClanByClanId(es *elasticsearch.Client, clanId int64) error {
	query := fmt.Sprintf(`{
		"query": {
			"term": {
				"clanId": %d
			}
		}
	}`, clanId)

	res, err := es.DeleteByQuery(
		[]string{"users"},
		strings.NewReader(query),
		es.DeleteByQuery.WithContext(context.Background()),
	)
	if err != nil {
		return fmt.Errorf("Error deleting document: %s", err)
	}
	defer res.Body.Close()

	if res.IsError() {
		return fmt.Errorf("Error response from Elasticsearch: %s", res.String())
	}

	log.Printf("Successfully deleted user clan: %d", clanId)
	return nil
}

func deleteClanById(es *elasticsearch.Client, clanId int64) error {

	err := deleteServerUserClanByClanId(es, clanId)
	if err != nil {
		return nil
	}

	res, err := es.Delete(
		"clans", // Replace with your clans index name
		fmt.Sprintf("%d", clanId),
		es.Delete.WithRefresh("true"), // Refresh the index to make the delete visible immediately
	)
	if err != nil {
		return fmt.Errorf("Error deleting document: %s", err)
	}
	defer res.Body.Close()

	if res.IsError() {
		return fmt.Errorf("Error response from Elasticsearch: %s", res.String())
	}

	log.Printf("Successfully deleted clan: %d", clanId)
	return nil
}

func updatePopulationbyIslandID(esClient *elasticsearch.Client, islandID string, updatedPopulation int) error {
	query := fmt.Sprintf(`{
		"query": {
			"term": {
				"id": "%s"
			}
		}
	}`, islandID)

	searchRes, err := esClient.Search(
		esClient.Search.WithIndex("islands"),
		esClient.Search.WithBody(strings.NewReader(query)),
		esClient.Search.WithContext(context.Background()),
	)
	if err != nil {
		return fmt.Errorf("Error searching for document: %s", err)
	}
	defer searchRes.Body.Close()

	if searchRes.IsError() {
		return fmt.Errorf("Error response from Elasticsearch: %s", searchRes.String())
	}

	var searchResult map[string]interface{}
	if err := json.NewDecoder(searchRes.Body).Decode(&searchResult); err != nil {
		return fmt.Errorf("Error parsing search response: %s", err)
	}

	hits := searchResult["hits"].(map[string]interface{})["hits"].([]interface{})
	if len(hits) == 0 {
		return fmt.Errorf("Document not found for islandID: %s", islandID)
	}

	// Get the document ID
	docID := hits[0].(map[string]interface{})["_id"].(string)

	// Update the document by its ID
	updateRes, err := esClient.Update(
		"islands", // Replace with your islands index name
		docID,
		strings.NewReader(fmt.Sprintf(`{"doc": {"population": %d}}`, updatedPopulation)),
		esClient.Update.WithRefresh("true"), // Refresh the index to make the update visible immediately
	)

	if err != nil {
		return fmt.Errorf("Error updating document: %s", err)
	}
	defer updateRes.Body.Close()

	if updateRes.IsError() {
		return fmt.Errorf("Error response from Elasticsearch: %s", updateRes.String())
	}

	log.Printf("Successfully updated population for island: %s", islandID)
	return nil
}
