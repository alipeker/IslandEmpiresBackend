package com.islandempires.gameserverservice.model.building;

import com.islandempires.gameserverservice.enums.IslandBuildingEnum;
import com.islandempires.gameserverservice.model.buildinglevelspec.BarrackLevel;
import com.islandempires.gameserverservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.gameserverservice.model.buildingtype.MilitaryStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Barrack extends MilitaryStructures implements Serializable {
    private List<BarrackLevel> barrackLevelList;

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return barrackLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
