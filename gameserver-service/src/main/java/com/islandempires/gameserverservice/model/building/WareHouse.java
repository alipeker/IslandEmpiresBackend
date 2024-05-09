package com.islandempires.gameserverservice.model.building;

import com.islandempires.gameserverservice.enums.IslandBuildingEnum;
import com.islandempires.gameserverservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.gameserverservice.model.buildinglevelspec.WareHouseLevel;
import com.islandempires.gameserverservice.model.buildingtype.RawMaterialProductionStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WareHouse extends RawMaterialProductionStructures implements Serializable {
    private List<WareHouseLevel> wareHouseLevelList;

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return wareHouseLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
