package com.islandempires.buildingservice.shared.building;

import com.islandempires.buildingservice.shared.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.shared.buildinglevelspec.FisherLevel;
import com.islandempires.buildingservice.shared.buildingtype.FoodProductionStructures;
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
