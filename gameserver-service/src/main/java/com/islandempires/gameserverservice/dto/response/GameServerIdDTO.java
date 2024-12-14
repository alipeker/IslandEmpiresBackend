package com.islandempires.gameserverservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameServerIdDTO implements Serializable {
    private String id;
    private boolean enabled;
}
