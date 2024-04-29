package com.islandempires.gameserverservice.dto;

import com.islandempires.gameserverservice.model.IslandResource;
import com.islandempires.gameserverservice.model.building.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameServerDTO {
    private String id;
    private String serverName;

    private IslandResource islandResource;

    private Academia academia;
    private Barrack barrack;
    private BrickWorks brickWorks;
    private CannonCamp cannonCamp;
    private ClayMine clayMine;
    private DairyFarm dairyFarm;
    private DefenceTower defenceTower;
    private Embassy embassy;
    private Fisher fisher;
    private Foundry foundry;
    private Gunsmith gunsmith;
    private Houses houses;
    private IronMine ironMine;
    private IslandHeadquarter islandHeadquarter;
    private Mill mill;
    private RiffleBarrack riffleBarrack;
    private TimberCamp timberCamp;
    private WatchTower watchTower;

    private long timestamp;

    // Constructors, getters, and setters

    // You can also add any other methods or validations if needed
}
