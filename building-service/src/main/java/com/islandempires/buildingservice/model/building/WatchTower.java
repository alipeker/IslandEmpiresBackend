package com.islandempires.buildingservice.model.building;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.model.buildinglevelspec.WatchTowerLevel;
import com.islandempires.buildingservice.model.buildingtype.MilitaryStructures;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class WatchTower extends MilitaryStructures implements Serializable {
    private List<WatchTowerLevel> watchTowerLevelList;

    protected WatchTower() {
        this.islandBuildingEnum = IslandBuildingEnum.WATCH_TOWER;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return watchTowerLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
