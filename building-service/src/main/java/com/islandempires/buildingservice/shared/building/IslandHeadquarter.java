package com.islandempires.buildingservice.shared.building;

import com.islandempires.buildingservice.shared.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.shared.buildinglevelspec.IslandHeadquarterLevel;
import com.islandempires.buildingservice.shared.buildingtype.BasicStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IslandHeadquarter extends BasicStructures implements Serializable {
    private List<IslandHeadquarterLevel> islandHeadquarterLevelList;

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return islandHeadquarterLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
