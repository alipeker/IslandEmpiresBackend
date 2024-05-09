package com.islandempires.buildingservice.model.building;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.model.buildinglevelspec.IslandHeadquarterLevel;
import com.islandempires.buildingservice.model.buildingtype.BasicStructures;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class IslandHeadquarter extends BasicStructures implements Serializable {
    private List<IslandHeadquarterLevel> islandHeadquarterLevelList;

    protected IslandHeadquarter() {
        this.islandBuildingEnum = IslandBuildingEnum.ISLAND_HEADQUARTERS;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return islandHeadquarterLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
