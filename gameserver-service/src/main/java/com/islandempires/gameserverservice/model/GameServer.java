package com.islandempires.gameserverservice.model;

import com.islandempires.gameserverservice.model.building.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document
@Data
@RequiredArgsConstructor
@EqualsAndHashCode
public class GameServer implements Serializable {
    @Id
    private String id;

    private String serverName;

    private IslandResource islandResource;

    private AllBuildings allBuildings;

    private long timestamp = new Date().getTime();
}
