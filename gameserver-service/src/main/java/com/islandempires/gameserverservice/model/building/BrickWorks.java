package com.islandempires.gameserverservice.model.building;

import com.islandempires.gameserverservice.model.buildinglevelspec.BricksWorksLevel;
import com.islandempires.gameserverservice.model.buildinglevelspec.BuildingLevel;
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
public class BrickWorks extends RawMaterialProductionStructures implements Serializable {
    private List<BricksWorksLevel> bricksWorksLevelList;

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return bricksWorksLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
