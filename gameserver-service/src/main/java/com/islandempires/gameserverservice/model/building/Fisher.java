package com.islandempires.gameserverservice.model.building;

import com.islandempires.gameserverservice.enums.IslandBuildingEnum;
import com.islandempires.gameserverservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.gameserverservice.model.buildinglevelspec.FisherLevel;
import com.islandempires.gameserverservice.model.buildingtype.FoodProductionStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fisher extends FoodProductionStructures implements Serializable {
    private List<FisherLevel> fisherLevelList;


    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return fisherLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
