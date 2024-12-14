package main

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"strconv"
	"strings"
	"time"

	"github.com/elastic/go-elasticsearch/v8"
	"github.com/gofiber/fiber/v2"
)

type IslandCombined struct {
	ID         string         `json:"id"`
	ServerID   string         `json:"serverId"`
	Name       string         `json:"name"`
	UserID     int64          `json:"userId"`
	X          int            `json:"x"`
	Y          int            `json:"y"`
	Population int            `json:"population"`
	Clan       PublicClanUser `json:"clan"`
}

type IslandDTO struct {
	ID       string `json:"id"`
	ServerID string `json:"serverId"`
	Name     string `json:"name"`
	UserID   int64  `json:"userId"`
	X        int    `json:"x"`
	Y        int    `json:"y"`
}

type IslandResourceDTO struct {
	IslandID        string `json:"islandId"`
	Population      int    `json:"population"`
	PopulationLimit int    `json:"populationLimit"`
}

type UserDTO struct {
	ID       int64  `json:"id"`
	Username string `json:"username"`
}

type ClanUser struct {
	ID              int64  `json:"userId"`
	Username        string `json:"username"`
	TotalPopulation int64  `json:"totalPopulation"`
	ServerId        string `json:"serverId"`
}

type ClanJoinType string

const (
	InviteOnly  ClanJoinType = "INVITE_ONLY"
	Public      ClanJoinType = "PUBLIC"
	Application ClanJoinType = "APPLICATION"
)

type PublicClan struct {
	ClanId          int64        `json:"clanId"`
	ClanName        string       `json:"clanName"`
	ClanShortName   string       `json:"clanShortName"`
	ClanDescription string       `json:"clanDescription"`
	UserList        []ClanUser   `json:"userList"`
	LocalDateTime   time.Time    `json:"localDateTime"`
	Founder         ClanUser     `json:"founder"`
	ClanJoinType    ClanJoinType `json:"clanJoinTypeEnum"`
	TotalPopulation int64        `json:"totalPopulation"`
	ServerId        string       `json:"serverId"`
}

type PublicClanUser struct {
	UserId   int64      `json:"userId"`
	UserName string     `json:"username"`
	ClanId   int64      `json:"clanId"`
	ServerId string     `json:"serverId"`
	Clan     PublicClan `json:"publicClanDTO"`
}

type UserIslandsCombined struct {
	UserID          int64            `json:"userId"`
	Username        string           `json:"username"`
	IslandList      []IslandCombined `json:"islandList"`
	TotalPopulation int              `json:"totalPopulation"`
	Clan            PublicClanUser   `json:"clan"`
}

type UsernameResult struct {
	Username     string `json:"username"`
	IslandNumber int64  `json:"islandnumber"`
	Population   int64  `json:"population"`
}

type ClanInfo struct {
	ClanID          int64  `json:"clanId"`
	TotalPopulation int64  `json:"total_population"`
	UniqueUsers     int64  `json:"unique_users"`
	ClanShortName   string `json:"clan_short_name"`
	ClanName        string `json:"clan_name"`
	ClanJoinType    string `json:"clan_join_type"`
}

type UserPopulation struct {
	UserId     int64  `json:"userId"`
	Username   string `json:"username"`
	Population int64  `json:"population"`
	ServerId   string `json:"serverId"`
}

func main() {
	// Create a new Fiber app
	app := fiber.New()

	app.Get("/map/public/:serverId/:xStart/:xEnd/:yStart/:yEnd", getIslandsHandler)

	app.Get("/map/public/:serverId/:username", getUsersPopulationHandler)

	app.Get("/map/public/findByUserId/:serverId/:userId", getUserPublicInfo)

	app.Post("/map/public/clan", func(c *fiber.Ctx) error {
		var request struct {
			ID int64 `json:"id"`
		}

		if err := c.BodyParser(&request); err != nil {
			return c.Status(fiber.StatusBadRequest).JSON(fiber.Map{
				"error": "Invalid request body",
			})
		}

		return getClanInfoByClanId(c, request.ID)
	})

	app.Post("/map/public/getClansByParameter", func(c *fiber.Ctx) error {
		var request struct {
			NAME string `json:"name"`
		}

		if err := c.BodyParser(&request); err != nil {
			return c.Status(fiber.StatusBadRequest).JSON(fiber.Map{
				"error": "Invalid request body",
			})
		}

		return getClansByParameter(c, request.NAME)
	})

	app.Post("/map/public/getClanWithId", func(c *fiber.Ctx) error {
		var request struct {
			ID int64 `json:"id"`
		}

		if err := c.BodyParser(&request); err != nil {
			return c.Status(fiber.StatusBadRequest).JSON(fiber.Map{
				"error": "Invalid request body",
			})
		}

		return getClanByClanId(c, request.ID)
	})

	app.Get("/map/public/getNearestClans/:serverId/:centerX/:centerY", getNearestClans)

	app.Post("/map/public/getUsersPopulation", func(c *fiber.Ctx) error {
		var request struct {
			IdList   []int64 `json:"idList"`
			ServerId string  `json:"serverId"`
		}

		if err := c.BodyParser(&request); err != nil {
			return c.Status(fiber.StatusBadRequest).JSON(fiber.Map{
				"error": "Invalid request body",
			})
		}
		es := getESClient()

		result, err := fetchTotalPopulationByUserIds(es, request.ServerId, request.IdList)
		if err != nil {
			return c.Status(fiber.StatusInternalServerError).JSON(fiber.Map{
				"error": err.Error()})
		}
		return c.JSON(result)
	})

	// Start the Fiber app on port 500
	log.Println("Starting server on port 500...")
	if err := app.Listen(":500"); err != nil {
		log.Fatalf("Error starting server: %s", err)
	}
}

func getESClient() *elasticsearch.Client {
	cfg := elasticsearch.Config{
		Addresses: []string{
			"http://localhost:9201", // Change the host and port as needed
		},
		Username: "elastic",              // Add your Elasticsearch username here
		Password: "xo5NB+mgtR9DFQWa9wd_", // Add your Elasticsearch password here
	}
	es, err := elasticsearch.NewClient(cfg)
	if err != nil {
		log.Fatalf("Error creating Elasticsearch client: %s", err)
	}
	return es
}

func searchIslandsByCoordinates(es *elasticsearch.Client, serverID string, xStart, xEnd, yStart, yEnd int) ([]IslandCombined, error) {
	query := fmt.Sprintf(`
	{
		"query": {
			"bool": {
				"must": [
					{ "match": { "serverId": "%s" } },
					{ "range": { "x": { "gte": %d, "lte": %d } } },
					{ "range": { "y": { "gte": %d, "lte": %d } } }
				]
			}
		},
		"aggs": {
			"unique_user_ids": {
				"terms": {
					"field": "userId",
					"size": 10000
				}
			}
		},
		"size": 10000
	}`, serverID, xStart, xEnd, yStart, yEnd)

	res, err := es.Search(
		es.Search.WithContext(context.Background()),
		es.Search.WithIndex("islands"),
		es.Search.WithBody(strings.NewReader(query)),
		es.Search.WithTrackTotalHits(true),
	)
	if err != nil {
		return nil, err
	}
	defer res.Body.Close()

	if res.IsError() {
		return nil, fmt.Errorf("Error response from Elasticsearch: %s", res.String())
	}

	var result struct {
		Hits struct {
			Hits []struct {
				Source IslandCombined `json:"_source"`
			} `json:"hits"`
		} `json:"hits"`
		Aggregations struct {
			UniqueUserIDs struct {
				Buckets []struct {
					Key int64 `json:"key"`
				} `json:"buckets"`
			} `json:"unique_user_ids"` // Corrected to match the query's aggregation name
		} `json:"aggregations"`
	}

	// Decode the response into the struct
	if err := json.NewDecoder(res.Body).Decode(&result); err != nil {
		return nil, fmt.Errorf("Error decoding Elasticsearch response: %v", err)
	}

	// Process the data as needed
	userIDs := []int64{}
	for _, bucket := range result.Aggregations.UniqueUserIDs.Buckets {
		userIDs = append(userIDs, bucket.Key)
	}

	userMap, err := fetchUsersByUserId(es, userIDs, serverID)
	if err != nil {
		return nil, err
	}

	clanIds := []int64{}

	for _, user := range userMap {
		clanIds = append(clanIds, user.ClanId)
	}

	clanMap, err := fetchClansByIds(es, clanIds)
	if err != nil {
		return nil, err
	}

	var islands []IslandCombined
	for _, hit := range result.Hits.Hits {
		var island = hit.Source
		userp := PublicClanUser{
			UserId:   userMap[island.UserID].UserId,
			UserName: userMap[island.UserID].UserName,
			Clan:     clanMap[userMap[island.UserID].ClanId],
		}
		if user, ok := userMap[island.UserID]; ok {
			island.UserID = user.UserId
		}
		island.Clan = userp
		islands = append(islands, island)
	}

	return islands, nil
}

func searchUsersByUsername(es *elasticsearch.Client, serverId string, username string) ([]UserIslandsCombined, error) {

	query := fmt.Sprintf(`
	{
		"query": {
			"bool": {
				"must": [
					{ "match": { "serverId": "%s" } },
					{ "wildcard": { "username": { "value": "*%s*" } } }
				]
			}
		}
	}`, serverId, username)

	res, err := es.Search(
		es.Search.WithContext(context.Background()),
		es.Search.WithIndex("users"), // Replace with your users index name
		es.Search.WithBody(strings.NewReader(query)),
		es.Search.WithTrackTotalHits(true),
	)
	if err != nil {
		return nil, err
	}
	defer res.Body.Close()

	if res.IsError() {
		return nil, fmt.Errorf("Error response from Elasticsearch: %s", res.String())
	}

	var result struct {
		Hits struct {
			Hits []struct {
				Source ClanUser `json:"_source"`
			} `json:"hits"`
		} `json:"hits"`
	}

	if err := json.NewDecoder(res.Body).Decode(&result); err != nil {
		return nil, err
	}

	var users = result.Hits.Hits

	userIds := make([]int64, len(users))
	for i, user := range users {
		userIds[i] = user.Source.ID
	}

	clanMap, err := fetchClansByIds(es, userIds)
	fmt.Printf("ClanMap: %v\n", clanMap)

	islandsQuery := fmt.Sprintf(`
	{
		"query": {
			"bool": {
				"must": [
					{ "terms": { "userId": [%s] } },
					{ "term": { "serverId": "%s" } }
				]
			}
		}
	}`, strings.Trim(strings.Join(strings.Fields(fmt.Sprint(userIds)), ","), "[]"), serverId)

	islandsRes, err := es.Search(
		es.Search.WithContext(context.Background()),
		es.Search.WithIndex("islands"), // Replace with your islands index name
		es.Search.WithBody(strings.NewReader(islandsQuery)),
		es.Search.WithTrackTotalHits(true),
	)
	if err != nil {
		return nil, err
	}
	defer islandsRes.Body.Close()

	if islandsRes.IsError() {
		return nil, fmt.Errorf("Error response from Elasticsearch: %s", islandsRes.String())
	}

	var islandsResult struct {
		Hits struct {
			Hits []struct {
				Source IslandCombined `json:"_source"`
			} `json:"hits"`
		} `json:"hits"`
	}

	if err := json.NewDecoder(islandsRes.Body).Decode(&islandsResult); err != nil {
		return nil, err
	}

	userMap := make(map[int64]UserIslandsCombined)
	for _, user := range users {
		userMap[user.Source.ID] = UserIslandsCombined{
			UserID:     user.Source.ID,
			Username:   user.Source.Username,
			IslandList: []IslandCombined{},
		}
	}

	for _, hit := range islandsResult.Hits.Hits {
		island := hit.Source
		user := userMap[island.UserID]
		user.IslandList = append(user.IslandList, island)
		user.TotalPopulation += island.Population
		userMap[island.UserID] = user
	}

	var userIslandsCombinedList []UserIslandsCombined
	for _, user := range userMap {
		userIslandsCombinedList = append(userIslandsCombinedList, user)
	}

	return userIslandsCombinedList, nil
}

func fetchClansByIds(es *elasticsearch.Client, clanIds []int64) (map[int64]PublicClan, error) {
	// Build terms query for clanIds
	idsQuery := strings.Trim(strings.Join(strings.Fields(fmt.Sprint(clanIds)), ","), "[]")

	query := fmt.Sprintf(`
	{
		"size": 10000,
		"query": {
			"terms": {
				"clanId": [%s]
			}
		}
	}`, idsQuery)

	// Elasticsearch request
	res, err := es.Search(
		es.Search.WithContext(context.Background()),
		es.Search.WithIndex("clans"), // Replace with your clans index name
		es.Search.WithBody(strings.NewReader(query)),
	)
	if err != nil {
		return nil, err
	}
	defer res.Body.Close()

	if res.IsError() {
		return nil, fmt.Errorf("Error response from Elasticsearch: %s", res.String())
	}

	// Parse response
	var result struct {
		Hits struct {
			Hits []struct {
				Source PublicClan `json:"_source"`
			} `json:"hits"`
		} `json:"hits"`
	}

	if err := json.NewDecoder(res.Body).Decode(&result); err != nil {
		return nil, err
	}

	// Create map of ClanId to PublicClan
	clanMap := make(map[int64]PublicClan)
	for _, hit := range result.Hits.Hits {
		clan := hit.Source
		clanMap[clan.ClanId] = clan
	}

	return clanMap, nil
}

func fetchClanById(es *elasticsearch.Client, clanId int64) (PublicClan, error) {
	// Build term query for clanId
	query := fmt.Sprintf(`
	{
		"query": {
			"term": {
				"clanId": %d
			}
		}
	}`, clanId)

	// Elasticsearch request
	res, err := es.Search(
		es.Search.WithContext(context.Background()),
		es.Search.WithIndex("clans"), // Replace with your clans index name
		es.Search.WithBody(strings.NewReader(query)),
	)
	if err != nil {
		return PublicClan{}, err
	}
	defer res.Body.Close()

	if res.IsError() {
		return PublicClan{}, fmt.Errorf("Error response from Elasticsearch: %s", res.String())
	}

	// Parse response
	var result struct {
		Hits struct {
			Hits []struct {
				Source PublicClan `json:"_source"`
			} `json:"hits"`
		} `json:"hits"`
	}

	if err := json.NewDecoder(res.Body).Decode(&result); err != nil {
		return PublicClan{}, err
	}

	// Check if we got any results
	if len(result.Hits.Hits) == 0 {
		return PublicClan{}, fmt.Errorf("No clan found for clanId: %d", clanId)
	}

	// Return the first (and only) result
	return result.Hits.Hits[0].Source, nil
}

func getIslandsHandler(c *fiber.Ctx) error {
	// Extract path variables from the request URL
	serverID := c.Params("serverId")
	xStart, err := strconv.Atoi(c.Params("xStart"))
	if err != nil {
		return fiber.NewError(fiber.StatusBadRequest, "Invalid xStart")
	}
	xEnd, err := strconv.Atoi(c.Params("xEnd"))
	if err != nil {
		return fiber.NewError(fiber.StatusBadRequest, "Invalid xEnd")
	}
	yStart, err := strconv.Atoi(c.Params("yStart"))
	if err != nil {
		return fiber.NewError(fiber.StatusBadRequest, "Invalid yStart")
	}
	yEnd, err := strconv.Atoi(c.Params("yEnd"))
	if err != nil {
		return fiber.NewError(fiber.StatusBadRequest, "Invalid yEnd")
	}

	// Initialize Elasticsearch client
	es := getESClient()

	// Call service to search for islands
	islands, err := searchIslandsByCoordinates(es, serverID, xStart, xEnd, yStart, yEnd)
	if err != nil {
		return fiber.NewError(fiber.StatusInternalServerError, err.Error())
	}

	// Send response as JSON
	return c.JSON(islands)
}

func getUsersPopulationHandler(c *fiber.Ctx) error {
	// Extract path variable from the request URL

	serverId := c.Params("serverId")

	username := c.Params("username")

	// Initialize Elasticsearch client
	es := getESClient()

	// Call service to search for users and their population
	result, err := searchUsersByUsername(es, serverId, username)
	if err != nil {
		return fiber.NewError(fiber.StatusInternalServerError, err.Error())
	}

	// Send response as JSON
	return c.JSON(result)
}

func searchUserByUserId(es *elasticsearch.Client, serverId string, userId int) (*UserIslandsCombined, error) {
	query := fmt.Sprintf(`
	{
		"query": {
			"bool": {
				"must": [
					{ "match": { "userId": %d } },
					{ "match": { "serverId": "%s" } }
				]
			}
		}
	}`, userId, serverId)

	res, err := es.Search(
		es.Search.WithContext(context.Background()),
		es.Search.WithIndex("users"),
		es.Search.WithBody(strings.NewReader(query)),
		es.Search.WithTrackTotalHits(true),
	)
	if err != nil {
		return nil, err
	}
	defer res.Body.Close()

	if res.IsError() {
		return nil, fmt.Errorf("Error response from Elasticsearch: %s", res.String())
	}

	var result struct {
		Hits struct {
			Hits []struct {
				Source PublicClanUser `json:"_source"`
			} `json:"hits"`
		} `json:"hits"`
	}

	if err := json.NewDecoder(res.Body).Decode(&result); err != nil {
		return nil, err
	}

	if len(result.Hits.Hits) == 0 {
		return nil, nil
	}

	user := result.Hits.Hits[0].Source

	if user.ClanId != 0 {
		clan, err := fetchClanById(es, user.ClanId)
		if err != nil {
			return nil, err
		}
		user.Clan = clan
	}

	// Fetch islands for the user
	islandsQuery := fmt.Sprintf(`
		{
			"query": {
				"bool": {
					"must": [
						{ "term": { "userId": %d } },
						{ "term": { "serverId": "%s" } }
					]
				}
			}
		}`, userId, serverId)

	islandsRes, err := es.Search(
		es.Search.WithContext(context.Background()),
		es.Search.WithIndex("islands"), // Replace with your islands index name
		es.Search.WithBody(strings.NewReader(islandsQuery)),
		es.Search.WithTrackTotalHits(true),
	)
	if err != nil {
		return nil, err
	}
	defer islandsRes.Body.Close()

	if islandsRes.IsError() {
		return nil, fmt.Errorf("Error response from Elasticsearch: %s", islandsRes.String())
	}

	var islandsResult struct {
		Hits struct {
			Hits []struct {
				Source IslandCombined `json:"_source"`
			} `json:"hits"`
		} `json:"hits"`
	}

	if err := json.NewDecoder(islandsRes.Body).Decode(&islandsResult); err != nil {
		return nil, err
	}

	userIslandsCombined := &UserIslandsCombined{
		UserID:          user.UserId,
		Username:        user.UserName,
		IslandList:      []IslandCombined{},
		Clan:            user,
		TotalPopulation: 0,
	}

	for _, hit := range islandsResult.Hits.Hits {
		island := hit.Source
		userIslandsCombined.IslandList = append(userIslandsCombined.IslandList, island)
		userIslandsCombined.TotalPopulation += island.Population
	}

	return userIslandsCombined, nil
}

func getUserPublicInfo(c *fiber.Ctx) error {
	serverId := c.Params("serverId")

	userId, err := strconv.Atoi(c.Params("userId"))
	if err != nil {
		return fiber.NewError(fiber.StatusBadRequest, "Invalid userId")
	}

	es := getESClient()

	result, err := searchUserByUserId(es, serverId, userId)
	if err != nil {
		return fiber.NewError(fiber.StatusInternalServerError, err.Error())
	}

	return c.JSON(result)
}

func getNearestClans(c *fiber.Ctx) error {
	centerX, err := strconv.Atoi(c.Params("centerX"))
	if err != nil {
		return fiber.NewError(fiber.StatusBadRequest, "Invalid centerX")
	}

	centerY, err := strconv.Atoi(c.Params("centerY"))
	if err != nil {
		return fiber.NewError(fiber.StatusBadRequest, "Invalid centerY")
	}

	serverId := c.Params("serverId")

	es := getESClient()

	result, err := getNearestClansRequest(es, serverId, centerX, centerY, 100)
	if err != nil {
		return fiber.NewError(fiber.StatusInternalServerError, err.Error())
	}

	return c.JSON(result)
}

func contains(slice []int64, item int64) bool {
	for _, v := range slice {
		if v == item {
			return true
		}
	}
	return false
}

func getNearestClansRequest(es *elasticsearch.Client, serverId string, centerX, centerY, radius int) ([]ClanInfo, error) {
	query := fmt.Sprintf(`{
		"size": 0,
		"query": {
			"bool": {
				"must": [
					{ "match": { "serverId": "%s" } },
					{
						"script": {
							"script": {
								"source": "Math.pow(doc['x'].value - params.centerX, 2) + Math.pow(doc['y'].value - params.centerY, 2) <= Math.pow(params.radius, 2)",
								"params": {
									"centerX": %d,
									"centerY": %d,
									"radius": %d
								}
							}
						}
					}
				]
			}
		},
		"aggs": {
			"unique_user_ids": {
				"terms": {
					"field": "userId",
					"size": 10000
				}
			}
		}
	}`, serverId, centerX, centerY, radius)

	res, err := es.Search(
		es.Search.WithContext(context.Background()),
		es.Search.WithIndex("islands"),
		es.Search.WithBody(strings.NewReader(query)),
		es.Search.WithTrackTotalHits(true),
	)
	if err != nil {
		return nil, err
	}
	defer res.Body.Close()

	if res.IsError() {
		return nil, fmt.Errorf("Error response from Elasticsearch: %s", res.String())
	}

	var result struct {
		Hits struct {
			Hits []struct {
				Source IslandCombined `json:"_source"`
			} `json:"hits"`
		} `json:"hits"`
		Aggregations struct {
			UniqueUserIDs struct {
				Buckets []struct {
					Key int64 `json:"key"`
				} `json:"buckets"`
			} `json:"unique_user_ids"` // Corrected to match the query's aggregation name
		} `json:"aggregations"`
	}

	// Decode the response into the struct
	if err := json.NewDecoder(res.Body).Decode(&result); err != nil {
		return nil, fmt.Errorf("Error decoding Elasticsearch response: %v", err)
	}

	// Process the data as needed
	userIDs := []int64{}
	for _, bucket := range result.Aggregations.UniqueUserIDs.Buckets {
		userIDs = append(userIDs, bucket.Key)
	}

	userMap, err := fetchUsersByUserId(es, userIDs, serverId)

	clanIds := []int64{}
	for _, user := range userMap {
		if !contains(clanIds, user.ClanId) {
			clanIds = append(clanIds, user.ClanId)
		}
	}

	clanStats, err := fetchClansInfoList(es, clanIds, serverId)

	var clanInfoList []ClanInfo

	for _, clan := range clanStats {
		clan.TotalPopulation, err = fetchTotalPopulationofClanMembers(es, clan.ClanID)
		clan.UniqueUsers, err = fetchTotalUserInClan(es, clan.ClanID, serverId)
		clanInfoList = append(clanInfoList, clan)
	}
	return clanInfoList, nil
}

func fetchTotalUserInClan(es *elasticsearch.Client, clanId int64, serverId string) (int64, error) {
	// fetch in users index with clanId and serverId
	query := fmt.Sprintf(`
		{
			"query": {
				"bool": {
					"must": [
						{ "term": { "clanId": %d } },
						{ "term": { "serverId": "%s" } }
					]
				}
			}
		}`, clanId, serverId)

	res, err := es.Search(
		es.Search.WithContext(context.Background()),
		es.Search.WithIndex("users"), // Replace with your users index name
		es.Search.WithBody(strings.NewReader(query)),
		es.Search.WithTrackTotalHits(true),
	)
	if err != nil {
		return 0, err
	}
	defer res.Body.Close()

	if res.IsError() {
		return 0, fmt.Errorf("Error response from Elasticsearch: %s", res.String())
	}

	var result struct {
		Hits struct {
			Total struct {
				Value int64 `json:"value"`
			} `json:"total"`
		} `json:"hits"`
	}

	if err := json.NewDecoder(res.Body).Decode(&result); err != nil {
		return 0, err
	}

	return result.Hits.Total.Value, nil
}

func fetchClansInfoList(es *elasticsearch.Client, clanIds []int64, serverId string) (map[int64]ClanInfo, error) {
	query := fmt.Sprintf(`
    {
        "query": {
            "bool": {
                "must": [
                    { "terms": { "clanId": [%s] } }
                ]
            }
        }
    }`, strings.Trim(strings.Join(strings.Fields(fmt.Sprint(clanIds)), ","), "[]"))

	res, err := es.Search(
		es.Search.WithContext(context.Background()),
		es.Search.WithIndex("clans"), // Replace with your clans index name
		es.Search.WithBody(strings.NewReader(query)),
		es.Search.WithTrackTotalHits(true),
	)
	if err != nil {
		return nil, err
	}
	defer res.Body.Close()

	if res.IsError() {
		return nil, fmt.Errorf("Error response from Elasticsearch: %s", res.String())
	}

	var result struct {
		Hits struct {
			Hits []struct {
				Source struct {
					ClanID        int64  `json:"clanId"`
					ClanName      string `json:"clanName"`
					ClanShortName string `json:"clanShortName"`
					ClanJoinType  string `json:"clanJoinTypeEnum"`
				} `json:"_source"`
			} `json:"hits"`
		} `json:"hits"`
	}

	if err := json.NewDecoder(res.Body).Decode(&result); err != nil {
		return nil, err
	}

	clanInfoMap := make(map[int64]ClanInfo)
	for _, hit := range result.Hits.Hits {
		clanInfo := ClanInfo{
			ClanID:        hit.Source.ClanID,
			ClanName:      hit.Source.ClanName,
			ClanShortName: hit.Source.ClanShortName,
			ClanJoinType:  hit.Source.ClanJoinType,
		}
		clanInfoMap[clanInfo.ClanID] = clanInfo
	}

	return clanInfoMap, nil
}

func fetchTotalPopulationofClanMembers(es *elasticsearch.Client, clanId int64) (int64, error) {
	query := fmt.Sprintf(`
		{
			"query": {
				"bool": {
					"must": [
						{ "term": { "clanId": %d } }
					]
				}
			}
		}`, clanId)

	res, err := es.Search(
		es.Search.WithContext(context.Background()),
		es.Search.WithIndex("users"), // Replace with your users index name
		es.Search.WithBody(strings.NewReader(query)),
		es.Search.WithTrackTotalHits(true),
	)
	if err != nil {
		return 0, err
	}
	defer res.Body.Close()

	if res.IsError() {
		return 0, fmt.Errorf("Error response from Elasticsearch: %s", res.String())
	}

	var result struct {
		Hits struct {
			Hits []struct {
				Source struct {
					UserID   int64  `json:"userId"`
					ServerId string `json:"serverId"`
				} `json:"_source"`
			} `json:"hits"`
		} `json:"hits"`
	}

	if err := json.NewDecoder(res.Body).Decode(&result); err != nil {
		return 0, err
	}

	userIds := make([]int64, len(result.Hits.Hits))
	for i, hit := range result.Hits.Hits {
		userIds[i] = hit.Source.UserID
	}

	serverId := ""
	if len(result.Hits.Hits) > 0 {
		serverId = result.Hits.Hits[0].Source.ServerId
	}

	return fetchTotalPopulationOfUsers(es, userIds, serverId)
}

func fetchTotalPopulationOfUsers(es *elasticsearch.Client, userIds []int64, serverId string) (int64, error) {
	// Convert the userIds slice to a string representation
	userIdsStr := strings.Trim(strings.Join(strings.Fields(fmt.Sprint(userIds)), ","), "[]")

	// Create the query
	query := fmt.Sprintf(`
	{
		"query": {
			"bool": {
				"must": [
					{ "terms": { "userId": [%s] } },
					{ "term": { "serverId": "%s" } }
				]
			}
		},
		"aggs": {
			"total_population": {
				"sum": {
					"field": "population"
				}
			}
		}
	}`, userIdsStr, serverId)

	// Execute the search request
	res, err := es.Search(
		es.Search.WithContext(context.Background()),
		es.Search.WithIndex("islands"),
		es.Search.WithBody(strings.NewReader(query)),
		es.Search.WithTrackTotalHits(true),
	)
	if err != nil {
		return 0, fmt.Errorf("failed to execute search: %w", err)
	}
	defer res.Body.Close()

	// Check for Elasticsearch response errors
	if res.IsError() {
		var e map[string]interface{}
		if err := json.NewDecoder(res.Body).Decode(&e); err != nil {
			return 0, fmt.Errorf("error parsing Elasticsearch error response: %w", err)
		}
		return 0, fmt.Errorf("elasticsearch error: %v", e)
	}

	// Parse the response body
	var result struct {
		Aggregations struct {
			TotalPopulation struct {
				Value float64 `json:"value"`
			} `json:"total_population"`
		} `json:"aggregations"`
	}
	if err := json.NewDecoder(res.Body).Decode(&result); err != nil {
		return 0, fmt.Errorf("failed to decode Elasticsearch response: %w", err)
	}

	// Return the total population as an int64
	return int64(result.Aggregations.TotalPopulation.Value), nil
}

func getClanInfoByClanId(c *fiber.Ctx, clanId int64) error {
	es := getESClient()

	result, err := getUserListWithPopulation(c, es, clanId)
	if err != nil {
		return fiber.NewError(fiber.StatusInternalServerError, err.Error())
	}

	return c.JSON(result)
}

func getUserListWithPopulation(c *fiber.Ctx, es *elasticsearch.Client, clanId int64) ([]ClanUser, error) {
	query := fmt.Sprintf(`
	{
		"query": {
			"term": {
				"clanId": %d
			}
		}
	}`, clanId)

	res, err := es.Search(
		es.Search.WithContext(context.Background()),
		es.Search.WithIndex("users"),
		es.Search.WithBody(strings.NewReader(query)),
		es.Search.WithTrackTotalHits(true),
	)
	if err != nil {
		return nil, err
	}
	defer res.Body.Close()

	if res.IsError() {
		return nil, fmt.Errorf("Error response from Elasticsearch: %s", res.String())
	}

	var result struct {
		Hits struct {
			Hits []struct {
				ClanUser `json:"_source"`
			} `json:"hits"`
		} `json:"hits"`
	}

	if err := json.NewDecoder(res.Body).Decode(&result); err != nil {
		return nil, err
	}

	users := []ClanUser{}
	var userIds []int64
	for _, hit := range result.Hits.Hits {
		users = append(users, hit.ClanUser)
		userIds = append(userIds, hit.ID)
	}

	serverId := ""
	if len(users) > 0 {
		serverId = users[0].ServerId
	}

	userPopulationMap, err := fetchTotalPopulationByUserIds(es, serverId, userIds)

	for i := range users {
		users[i].TotalPopulation = userPopulationMap[users[i].ID]
	}

	return users, nil
}

func getClansByParameter(c *fiber.Ctx, clanNameOrShortname string) error {
	es := getESClient()

	query := fmt.Sprintf(`
	{
		"query": {
			"bool": {
				"should": [
					{
						"wildcard": {
							"clanName.keyword": {
								"value": "*%s*"
							}
						}
					},
					{
						"wildcard": {
							"clanShortName.keyword": {
								"value": "*%s*",
								"case_insensitive": true
							}
						}
					}
				],
				"minimum_should_match": 1
			}
		},
		"aggs": {
			"unique_clan_names": {
				"terms": {
					"field": "clanId",
					"size": 10
				},
				"aggs": {
					"top_clan_hits": {
						"top_hits": {
							"size": 1,
							"_source": ["clanName", "clanShortName"]
						}
					}
				}
			}
		},
		"size": 0
	}`, clanNameOrShortname, clanNameOrShortname)

	res, err := es.Search(
		es.Search.WithContext(context.Background()),
		es.Search.WithIndex("clans"),
		es.Search.WithBody(strings.NewReader(query)),
	)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).SendString("Error fetching data from Elasticsearch")
	}
	defer res.Body.Close()

	if res.IsError() {
		return c.Status(fiber.StatusInternalServerError).SendString(fmt.Sprintf("Error response from Elasticsearch: %s", res.String()))
	}

	var result struct {
		Aggregations struct {
			UniqueClanNames struct {
				Buckets []struct {
					Key     int64 `json:"key"`
					TopHits struct {
						Hits struct {
							Hits []struct {
								Source struct {
									ClanName      string `json:"clanName"`
									ClanShortName string `json:"clanShortName"`
								} `json:"_source"`
							} `json:"hits"`
						} `json:"hits"`
					} `json:"top_clan_hits"`
				} `json:"buckets"`
			} `json:"unique_clan_names"`
		} `json:"aggregations"`
	}

	if err := json.NewDecoder(res.Body).Decode(&result); err != nil {
		return c.Status(fiber.StatusInternalServerError).SendString("Error parsing the response body")
	}

	clans := []ClanInfo{}
	for _, bucket := range result.Aggregations.UniqueClanNames.Buckets {
		clans = append(clans, ClanInfo{
			ClanID:        bucket.Key,
			ClanShortName: bucket.TopHits.Hits.Hits[0].Source.ClanShortName,
			ClanJoinType:  bucket.TopHits.Hits.Hits[0].Source.ClanName,
		})
	}

	return c.JSON(clans)
}

func getClanByClanId(c *fiber.Ctx, clanId int64) error {
	es := getESClient()

	result, err := getClanByClanIdQuery(c, es, clanId)
	if err != nil {
		return fiber.NewError(fiber.StatusInternalServerError, err.Error())
	}

	return c.JSON(result)
}

func getClanByClanIdQuery(c *fiber.Ctx, es *elasticsearch.Client, clanId int64) (PublicClan, error) {
	query := fmt.Sprintf(`
	{
		"query": {
			"term": {
				"clanId": %d
			}
		}
	}`, clanId)

	res, err := es.Search(
		es.Search.WithContext(context.Background()),
		es.Search.WithIndex("clans"),
		es.Search.WithBody(strings.NewReader(query)),
	)
	if err != nil {
		return PublicClan{}, err
	}
	defer res.Body.Close()

	if res.IsError() {
		return PublicClan{}, fmt.Errorf("Error response from Elasticsearch: %s", res.String())
	}

	var result struct {
		Hits struct {
			Hits []struct {
				Source PublicClan `json:"_source"`
			} `json:"hits"`
		} `json:"hits"`
	}

	if err := json.NewDecoder(res.Body).Decode(&result); err != nil {
		return PublicClan{}, err
	}

	if len(result.Hits.Hits) == 0 {
		return PublicClan{}, fmt.Errorf("No clan found for clanId: %d", clanId)
	}

	publicClan := result.Hits.Hits[len(result.Hits.Hits)-1].Source

	users, err := getUserListWithPopulation(c, es, clanId)

	publicClan.UserList = users

	// userIds := []int64{}

	// for _, user := range publicClan.UserList {
	// 	userIds = append(userIds, user.ID)
	// }

	// userPopulationMap, err := fetchTotalPopulationByUserIds(es, publicClan.ServerId, userIds)

	// for i := range publicClan.UserList {
	// 	publicClan.UserList[i].TotalPopulation = userPopulationMap[publicClan.UserList[i].ID]
	// }

	return publicClan, nil
}

func fetchUsersByUserId(es *elasticsearch.Client, userIdList []int64, serverId string) (map[int64]PublicClanUser, error) {
	query := fmt.Sprintf(`
        {
            "size": 10000,
            "query": {
                "bool": {
                    "filter": [
                        { "terms": { "userId": [%s] } },
                        { "term": { "serverId.keyword": "%s" } }
                    ]
                }
            }
        }`, strings.Trim(strings.Join(strings.Fields(fmt.Sprint(userIdList)), ","), "[]"), serverId)

	res, err := es.Search(
		es.Search.WithContext(context.Background()),
		es.Search.WithIndex("users"), // Replace with your users index name
		es.Search.WithBody(strings.NewReader(query)),
		es.Search.WithTrackTotalHits(true),
	)
	if err != nil {
		return nil, err
	}
	defer res.Body.Close()

	if res.IsError() {
		return nil, fmt.Errorf("Error response from Elasticsearch: %s", res.String())
	}

	var result struct {
		Hits struct {
			Hits []struct {
				Source PublicClanUser `json:"_source"`
			} `json:"hits"`
		} `json:"hits"`
	}

	if err := json.NewDecoder(res.Body).Decode(&result); err != nil {
		return nil, err
	}

	userMap := make(map[int64]PublicClanUser)
	for _, hit := range result.Hits.Hits {
		user := hit.Source
		userMap[user.UserId] = user
	}

	return userMap, nil
}

func fetchTotalPopulationByUserIds(es *elasticsearch.Client, serverId string, userIdList []int64) (map[int64]int64, error) {
	// Convert the userIdList slice to a string representation
	userIdsStr := strings.Trim(strings.Join(strings.Fields(fmt.Sprint(userIdList)), ","), "[]")

	// Create the query
	query := fmt.Sprintf(`
	{
		"query": {
			"bool": {
				"must": [
					{ "terms": { "userId": [%s] } },
					{ "term": { "serverId": "%s" } }
				]
			}
		},
		"aggs": {
			"user_population": {
				"terms": {
					"field": "userId",
					"size": 10000
				},
				"aggs": {
					"total_population": {
						"sum": {
							"field": "population"
						}
					}
				}
			}
		},
		"size": 0
	}`, userIdsStr, serverId)

	// Execute the search request
	res, err := es.Search(
		es.Search.WithContext(context.Background()),
		es.Search.WithIndex("islands"),
		es.Search.WithBody(strings.NewReader(query)),
		es.Search.WithTrackTotalHits(true),
	)
	if err != nil {
		return nil, fmt.Errorf("failed to execute search: %w", err)
	}
	defer res.Body.Close()

	// Check for Elasticsearch response errors
	if res.IsError() {
		var e map[string]interface{}
		if err := json.NewDecoder(res.Body).Decode(&e); err != nil {
			return nil, fmt.Errorf("error parsing Elasticsearch error response: %w", err)
		}
		return nil, fmt.Errorf("elasticsearch error: %v", e)
	}

	// Parse the response body
	var result struct {
		Aggregations struct {
			UserPopulation struct {
				Buckets []struct {
					Key             int64 `json:"key"`
					TotalPopulation struct {
						Value float64 `json:"value"`
					} `json:"total_population"`
				} `json:"buckets"`
			} `json:"user_population"`
		} `json:"aggregations"`
	}
	if err := json.NewDecoder(res.Body).Decode(&result); err != nil {
		return nil, fmt.Errorf("failed to decode Elasticsearch response: %w", err)
	}

	// Create a map to store the total population for each userId
	populationMap := make(map[int64]int64)
	for _, bucket := range result.Aggregations.UserPopulation.Buckets {
		populationMap[bucket.Key] = int64(bucket.TotalPopulation.Value)
	}

	return populationMap, nil
}
