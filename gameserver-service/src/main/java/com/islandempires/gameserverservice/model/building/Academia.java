package com.islandempires.gameserverservice.model.building;

import com.islandempires.gameserverservice.enums.IslandBuildingEnum;
import com.islandempires.gameserverservice.model.buildinglevelspec.AcademiaLevel;
import com.islandempires.gameserverservice.model.buildingtype.BasicStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

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
    protected IslandBuildingEnum getBuildName() {
        return IslandBuildingEnum.ACADEMIA;
    }
}
