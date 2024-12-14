package main

import (
	"time"
)

type IslandResource struct {
	ID                           string    `bson:"id"`
	IslandID                     string    `bson:"islandId"`
	UserID                       int64     `bson:"userId"`
	ServerId                     string    `bson:"serverId"`
	Wood                         float64   `bson:"wood"`
	WoodHourlyProduction         int       `bson:"woodHourlyProduction"`
	Iron                         float64   `bson:"iron"`
	IronHourlyProduction         int       `bson:"ironHourlyProduction"`
	IronHourlyProductionMultiply int       `bson:"ironHourlyProductionMultiply"`
	Clay                         float64   `bson:"clay"`
	ClayHourlyProduction         int       `bson:"clayHourlyProduction"`
	ClayHourlyProductionMultiply int       `bson:"clayHourlyProductionMultiply"`
	Gold                         float64   `bson:"gold"`
	RawMaterialStorageSize       int       `bson:"rawMaterialStorageSize"`
	MeatFoodCoefficient          float64   `bson:"meatFoodCoefficient"`
	MeatHourlyProduction         int       `bson:"meatHourlyProduction"`
	FishFoodCoefficient          float64   `bson:"fishFoodCoefficient"`
	FishHourlyProduction         int       `bson:"fishHourlyProduction"`
	WheatFoodCoefficient         float64   `bson:"wheatFoodCoefficient"`
	WheatHourlyProduction        int       `bson:"wheatHourlyProduction"`
	Population                   int64     `bson:"population"`
	TemporaryPopulation          int64     `bson:"temporaryPopulation"`
	PopulationLimit              float64   `bson:"populationLimit"`
	HappinessScore               float64   `bson:"happinessScore"`
	AdditionalHappinessScore     float64   `bson:"additionalHappinessScore"`
	LastCalculatedTimestamp      int64     `bson:"lastCalculatedTimestamp"`
	CreatedDate                  time.Time `bson:"createdDate"`
}

type UserSession struct {
	ID             string           `bson:"_id"`
	UserID         int64            `bson:"userId"`
	JwtToken       string           `bson:"jwtToken"`
	ServerID       string           `bson:"serverId"`
	IslandResource []IslandResource `bson:"islandResourceList"`
}
