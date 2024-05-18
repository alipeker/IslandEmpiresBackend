package com.islandempires.buildingworker.model.buildingtype;

import com.islandempires.buildingworker.enums.IslandBuildingEnum;
import com.islandempires.buildingworker.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingworker.model.strategy.BuildingStrategy;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;


@Data
public abstract class BaseStructures implements Serializable, BuildingStrategy {

    @Id
    protected IslandBuildingEnum islandBuildingEnum;

    private LocalDateTime createdAt;

    private int initialLvl = 0;

    private HashMap<IslandBuildingEnum, Integer> upgradeConditions;

    @Autowired
    protected RestTemplate restTemplate;

    public abstract List<BuildingLevel> getBuildingLevelList();
}
