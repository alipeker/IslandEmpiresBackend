package com.islandempires.gameserverservice.model.buildingtype;

import com.islandempires.gameserverservice.enums.IslandBuildingEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;


@Data
public abstract class BaseStructures implements Serializable {
    private LocalDateTime createdAt;

    private HashMap<IslandBuildingEnum, Integer> upgradeConditions;

    protected abstract IslandBuildingEnum getBuildName();
}
