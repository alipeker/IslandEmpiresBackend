package com.islandempires.buildingservice.model.building;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.model.buildinglevelspec.DefenceTowerLevel;
import com.islandempires.buildingservice.model.buildingtype.MilitaryStructures;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class DefenceTower extends MilitaryStructures implements Serializable {
    private List<DefenceTowerLevel> defenceTowerLevelList;

    protected DefenceTower() {
        this.islandBuildingEnum = IslandBuildingEnum.DEFENCE_TOWER;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return defenceTowerLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
