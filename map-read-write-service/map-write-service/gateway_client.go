package main

import (
	"encoding/json"
	"fmt"
	"net/http"
)

type GatewayClient struct {
	baseURL string
}

func NewGatewayClient(baseURL string) *GatewayClient {
	return &GatewayClient{baseURL: baseURL}
}

func (client *GatewayClient) GetIslands() ([]IslandDTO, error) {
	resp, err := http.Get(client.baseURL + "/island/private/getAll")
	if err != nil {
		fmt.Println("GetIslands Response: ", nil)
		return nil, err
	}
	fmt.Println("GetIslands Response: ")
	defer resp.Body.Close()

	var islands []IslandDTO
	if err := json.NewDecoder(resp.Body).Decode(&islands); err != nil {
		return nil, err
	}
	return islands, nil
}

func (client *GatewayClient) GetIslandResources() ([]IslandResourceDTO, error) {
	resp, err := http.Get(client.baseURL + "/resource/private/getAll")
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	var resources []IslandResourceDTO
	if err := json.NewDecoder(resp.Body).Decode(&resources); err != nil {
		return nil, err
	}
	return resources, nil
}

func (client *GatewayClient) GetIslandResource(islandId string) (IslandResourceDTO, error) {
	resp, err := http.Get(client.baseURL + "/resource/private/" + islandId)
	if err != nil {
		return IslandResourceDTO{}, err
	}
	defer resp.Body.Close()

	var resource IslandResourceDTO
	if err := json.NewDecoder(resp.Body).Decode(&resource); err != nil {
		return IslandResourceDTO{}, err
	}
	return resource, nil
}

func (client *GatewayClient) GetUsers() ([]UserDTO, error) {
	resp, err := http.Get(client.baseURL + "/auth/private/getAllUsers")
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	var users []UserDTO
	if err := json.NewDecoder(resp.Body).Decode(&users); err != nil {
		return nil, err
	}
	return users, nil
}

func (client *GatewayClient) GetUserClans() ([]PublicClanUser, error) {
	resp, err := http.Get(client.baseURL + "/clan/private/getUsers")
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	var clanUser []PublicClanUser
	if err := json.NewDecoder(resp.Body).Decode(&clanUser); err != nil {
		return nil, err
	}
	return clanUser, nil
}

func (client *GatewayClient) GetClanUser(userId int64, serverId string) (PublicClanUser, error) {
	resp, err := http.Get(client.baseURL + "/clan/private/getUser/" + fmt.Sprintf("%d", userId) + "/" + serverId)
	if err != nil {
		return PublicClanUser{}, err
	}
	defer resp.Body.Close()

	var clanUser PublicClanUser
	if err := json.NewDecoder(resp.Body).Decode(&clanUser); err != nil {
		return PublicClanUser{}, err
	}
	return clanUser, nil
}

func (client *GatewayClient) GetClans() ([]PublicClan, error) {
	resp, err := http.Get(client.baseURL + "/clan/private/getClans")
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	var clanUser []PublicClan
	if err := json.NewDecoder(resp.Body).Decode(&clanUser); err != nil {
		return nil, err
	}
	return clanUser, nil
}

func (client *GatewayClient) GetClan(clanId int64) (PublicClan, error) {
	url := fmt.Sprintf(client.baseURL+"/clan/private/getClan/%d", clanId)
	resp, err := http.Get(url)
	if err != nil {
		return PublicClan{}, err
	}
	defer resp.Body.Close()

	var clan PublicClan
	if err := json.NewDecoder(resp.Body).Decode(&clan); err != nil {
		return PublicClan{}, err
	}
	return clan, nil
}
