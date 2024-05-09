package com.islandempires.buildingservice.model.building;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.model.buildinglevelspec.WareHouseLevel;
import com.islandempires.buildingservice.model.buildingtype.RawMaterialProductionStructures;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class WareHouse extends RawMaterialProductionStructures implements Serializable {
    private List<WareHouseLevel> wareHouseLevelList;

    protected WareHouse() {
        this.islandBuildingEnum = IslandBuildingEnum.WAREHOUSE;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return wareHouseLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
