package com.islandempires.gameserverservice.model.building;

import com.islandempires.gameserverservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.gameserverservice.model.buildinglevelspec.ShipyardLevel;
import com.islandempires.gameserverservice.model.buildingtype.MilitaryStructures;
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

