package com.islandempires.buildingservice.model.building;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.model.buildinglevelspec.FisherLevel;
import com.islandempires.buildingservice.model.buildingtype.FoodProductionStructures;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Fisher extends FoodProductionStructures implements Serializable {
    private List<FisherLevel> fisherLevelList;

    protected Fisher() {
        this.islandBuildingEnum = IslandBuildingEnum.FISHER;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return fisherLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
