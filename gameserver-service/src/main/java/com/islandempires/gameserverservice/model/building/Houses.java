package com.islandempires.gameserverservice.model.building;

import com.islandempires.gameserverservice.enums.IslandBuildingEnum;
import com.islandempires.gameserverservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.gameserverservice.model.buildinglevelspec.HouseLevel;
import com.islandempires.gameserverservice.model.buildingtype.BasicStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Houses extends BasicStructures implements Serializable {
    private List<HouseLevel> houseLevelList;


    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return houseLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
