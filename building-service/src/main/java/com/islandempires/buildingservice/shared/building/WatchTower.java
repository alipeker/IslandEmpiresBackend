package com.islandempires.buildingservice.shared.building;

import com.islandempires.buildingservice.shared.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.shared.buildinglevelspec.WatchTowerLevel;
import com.islandempires.buildingservice.shared.buildingtype.MilitaryStructures;
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
