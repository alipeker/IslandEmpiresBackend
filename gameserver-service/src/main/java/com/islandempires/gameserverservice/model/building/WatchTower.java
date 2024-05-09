package com.islandempires.gameserverservice.model.building;

import com.islandempires.gameserverservice.enums.IslandBuildingEnum;
import com.islandempires.gameserverservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.gameserverservice.model.buildinglevelspec.WatchTowerLevel;
import com.islandempires.gameserverservice.model.buildingtype.MilitaryStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchTower extends MilitaryStructures implements Serializable {
    private List<WatchTowerLevel> watchTowerLevelList;

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return watchTowerLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
