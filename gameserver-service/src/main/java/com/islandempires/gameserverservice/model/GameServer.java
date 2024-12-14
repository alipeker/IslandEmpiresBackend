package com.islandempires.gameserverservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Document
@Data
@RequiredArgsConstructor
@EqualsAndHashCode
public class GameServer implements Serializable {
    @Id
    private String id;

    private String serverName;

    private GameServerIslandResource gameServerIslandResource;

    private GameServerAllBuildings gameServerAllBuildings;

    private GameServerSoldier gameServerSoldier;

    private LocalDateTime localDateTime;

}
