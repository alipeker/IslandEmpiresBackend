package com.islandempires.buildingservice.model.building;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.buildinglevelspec.BricksWorksLevel;
import com.islandempires.buildingservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.model.buildingtype.RawMaterialProductionStructures;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
public class BrickWorks extends RawMaterialProductionStructures implements Serializable {
    private List<BricksWorksLevel> bricksWorksLevelList;

    protected BrickWorks() {
        this.islandBuildingEnum = IslandBuildingEnum.BRICK_WORKS;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return bricksWorksLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
