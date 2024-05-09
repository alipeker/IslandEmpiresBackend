package com.islandempires.buildingservice.model.building;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.buildinglevelspec.BarrackLevel;
import com.islandempires.buildingservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.model.buildingtype.MilitaryStructures;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
public class Barrack extends MilitaryStructures implements Serializable {
    private List<BarrackLevel> barrackLevelList;

    protected Barrack() {
        this.islandBuildingEnum = IslandBuildingEnum.BARRACK;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return barrackLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
