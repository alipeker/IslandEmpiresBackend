package com.islandempires.websocketservice.model;

import lombok.Data;

@Data
public class WebSocketMessage {
    private String islandId;

    private String jwtToken;

}
