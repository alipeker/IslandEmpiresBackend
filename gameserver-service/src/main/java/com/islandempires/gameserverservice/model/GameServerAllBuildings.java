package com.islandempires.gameserverservice.model;

import com.islandempires.gameserverservice.model.building.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class GameServerAllBuildings implements Serializable {
    private String serverId;

    private AllBuildings allBuildings;

    private LocalDateTime timestamp = LocalDateTime.now();

}

