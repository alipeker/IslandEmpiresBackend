package com.islandempires.buildingservice.model.building;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.model.buildinglevelspec.FoundryLevel;
import com.islandempires.buildingservice.model.buildingtype.BasicStructures;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Foundry extends BasicStructures implements Serializable {
    private List<FoundryLevel> foundryLevelList;

    protected Foundry() {
        this.islandBuildingEnum = IslandBuildingEnum.FOUNDRY;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return foundryLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
