package main

type ClanMessage struct {
	ID   int         `json:"id"`
	Data interface{} `json:"data"`
}
