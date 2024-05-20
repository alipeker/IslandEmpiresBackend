package com.islandempires.buildingworker.shared.buildingtype;

import com.islandempires.buildingworker.client.GatewayClient;
import com.islandempires.buildingworker.enums.IslandBuildingEnum;
import com.islandempires.buildingworker.shared.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingworker.shared.strategypattern.BuildingLevelUpStrategy;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;


@Data
public abstract class BaseStructures implements Serializable, BuildingLevelUpStrategy {

    @Id
    protected IslandBuildingEnum islandBuildingEnum;

    private LocalDateTime createdAt;

    private int initialLvl = 0;

    private HashMap<IslandBuildingEnum, Integer> upgradeConditions;


    public abstract List<BuildingLevel> getBuildingLevelList();

}
