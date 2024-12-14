package main

import "time"

type IslandCombined struct {
	ID         string `json:"id"`
	ServerID   string `json:"serverId"`
	Name       string `json:"name"`
	UserID     int64  `json:"userId"`
	X          int    `json:"x"`
	Y          int    `json:"y"`
	Population int    `json:"population"`
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
	UserId          int64  `json:"userId"`
	ServerId        string `json:"serverId"`
}

type UserDTO struct {
	ID       int64  `json:"id"`
	Username string `json:"username"`
}

type ClanUser struct {
	ID       int64  `json:"userId"`
	Username string `json:"username"`
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
	LocalDateTime   time.Time    `json:"localDateTime"`
	Founder         ClanUser     `json:"founder"`
	ClanJoinType    ClanJoinType `json:"clanJoinTypeEnum"`
	ServerId        string       `json:"serverId"`
}

type PublicClanUser struct {
	UserId   int64  `json:"userId"`
	UserName string `json:"username"`
	ClanId   int64  `json:"clanId"`
	ServerId string `json:"serverId"`
}

type KafkaClanUser struct {
	UserId   int64  `json:"userId"`
	ServerId string `json:"serverId"`
}
