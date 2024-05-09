package com.islandempires.buildingservice.model.building;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.buildinglevelspec.AcademiaLevel;
import com.islandempires.buildingservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.model.buildingtype.BasicStructures;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/*
* This building is for researching new troops. When the user levels up, they can research new troops.
* With academia level user can have research point
* */
@Data
@AllArgsConstructor
public class Academia extends BasicStructures implements Serializable {

    private List<AcademiaLevel> academiaLevelList;

    protected Academia() {
        this.islandBuildingEnum = IslandBuildingEnum.ACADEMIA;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return academiaLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
