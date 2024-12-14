package com.islandempires.buildingservice.shared.building;

import com.islandempires.buildingservice.shared.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.shared.buildinglevelspec.ShipyardLevel;
import com.islandempires.buildingservice.shared.buildingtype.MilitaryStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shipyard extends MilitaryStructures {
    private List<ShipyardLevel> shipyardLevelList;

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return shipyardLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}

