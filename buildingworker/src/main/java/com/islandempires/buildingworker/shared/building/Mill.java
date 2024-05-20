package com.islandempires.buildingworker.shared.building;

import com.islandempires.buildingworker.shared.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingworker.shared.buildinglevelspec.MillLevel;
import com.islandempires.buildingworker.shared.buildingtype.FoodProductionStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mill extends FoodProductionStructures implements Serializable {
    private List<MillLevel> millLevelList;

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return millLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }

    @Override
    public void islandBuildingLevelUpExecution(int nextLvl, String islandId) {

    }
}
