package com.islandempires.buildingservice.model.building;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.model.buildinglevelspec.HouseLevel;
import com.islandempires.buildingservice.model.buildingtype.BasicStructures;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Houses extends BasicStructures implements Serializable {
    private List<HouseLevel> houseLevelList;

    protected Houses() {
        this.islandBuildingEnum = IslandBuildingEnum.HOUSES;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return houseLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
