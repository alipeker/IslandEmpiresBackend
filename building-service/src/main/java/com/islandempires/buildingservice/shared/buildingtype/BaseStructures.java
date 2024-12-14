package com.islandempires.buildingservice.shared.buildingtype;


import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.shared.buildinglevelspec.BuildingLevel;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;


@Data
public abstract class BaseStructures implements Serializable {

    @Id
    protected IslandBuildingEnum islandBuildingEnum;

    private LocalDateTime createdAt;

    private int initialLvl = 0;

    private HashMap<IslandBuildingEnum, Integer> upgradeConditions;


    public abstract List<BuildingLevel> getBuildingLevelList();
}
