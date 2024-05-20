package com.islandempires.buildingworker.shared.building;

import com.islandempires.buildingworker.shared.buildinglevelspec.AcademiaLevel;
import com.islandempires.buildingworker.shared.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingworker.shared.buildingtype.BasicStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/*
* This building is for researching new troops. When the user levels up, they can research new troops.
* With academia level user can have research point
* */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Academia extends BasicStructures implements Serializable {

    private List<AcademiaLevel> academiaLevelList;


    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return academiaLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }

    @Override
    public void islandBuildingLevelUpExecution(int nextLvl, String islandId) {

    }
}
