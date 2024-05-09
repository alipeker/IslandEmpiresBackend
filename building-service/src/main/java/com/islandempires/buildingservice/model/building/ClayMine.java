package com.islandempires.buildingservice.model.building;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.model.buildinglevelspec.ClayMineLevel;
import com.islandempires.buildingservice.model.buildingtype.RawMaterialProductionStructures;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class ClayMine extends RawMaterialProductionStructures implements Serializable {
    private List<ClayMineLevel> clayMineLevelList;

    protected ClayMine() {
        this.islandBuildingEnum = IslandBuildingEnum.CLAY_MINE;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return clayMineLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
