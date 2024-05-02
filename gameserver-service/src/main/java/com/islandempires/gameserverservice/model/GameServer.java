package com.islandempires.gameserverservice.model;

import com.islandempires.gameserverservice.model.building.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document
@Data
@RequiredArgsConstructor
@EqualsAndHashCode
public class GameServer implements Serializable {
    @Id
    private String id;

    @DBRef
    private List<Island> islands = new ArrayList<>();

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

    private long timestamp = new Date().getTime();
}
