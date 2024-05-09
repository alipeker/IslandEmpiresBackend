package com.islandempires.buildingservice.model.building;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.model.buildinglevelspec.IronMineLevel;
import com.islandempires.buildingservice.model.buildingtype.RawMaterialProductionStructures;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class IronMine extends RawMaterialProductionStructures implements Serializable {
    private List<IronMineLevel> ironMineLevelList;

    protected IronMine() {
        this.islandBuildingEnum = IslandBuildingEnum.IRON_MINE;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return ironMineLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
