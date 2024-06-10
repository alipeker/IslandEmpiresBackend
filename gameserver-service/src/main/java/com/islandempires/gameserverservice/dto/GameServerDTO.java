package com.islandempires.gameserverservice.dto;

import com.islandempires.gameserverservice.model.IslandResource;
import com.islandempires.gameserverservice.model.building.*;
import com.islandempires.gameserverservice.model.soldier.SoldierBaseInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameServerDTO {
    private String id;
    private String serverName;

    private IslandResource islandResource;

    private List<SoldierBaseInfo> soldierBaseInfoList;

    private AllBuildings allBuildings;

    private long timestamp;

    // Constructors, getters, and setters

    // You can also add any other methods or validations if needed
}
