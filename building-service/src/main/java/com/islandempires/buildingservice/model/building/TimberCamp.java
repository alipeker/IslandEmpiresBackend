package com.islandempires.buildingservice.model.building;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.model.buildinglevelspec.TimberCampLevel;
import com.islandempires.buildingservice.model.buildingtype.RawMaterialProductionStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimberCamp extends RawMaterialProductionStructures implements Serializable {
    private List<TimberCampLevel> timberCampLevelList;

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return timberCampLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
