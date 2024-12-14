package com.islandempires.websocketservice.model;

import lombok.Data;

@Data
public class WebSocketMessage {
    private String serverId;

    private String jwtToken;

    private Long userId;
}
