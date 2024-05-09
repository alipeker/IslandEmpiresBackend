package com.islandempires.buildingservice.model.building;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.model.buildinglevelspec.RiffleBarrackLevel;
import com.islandempires.buildingservice.model.buildingtype.MilitaryStructures;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class RiffleBarrack extends MilitaryStructures implements Serializable {
    private List<RiffleBarrackLevel> riffleBarrackLevelList;

    protected RiffleBarrack() {
        this.islandBuildingEnum = IslandBuildingEnum.RIFFLE_BARRACK;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return riffleBarrackLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
