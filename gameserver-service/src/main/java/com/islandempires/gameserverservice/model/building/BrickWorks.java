package com.islandempires.gameserverservice.model.building;

import com.islandempires.gameserverservice.enums.IslandBuildingEnum;
import com.islandempires.gameserverservice.model.buildinglevelspec.BricksWorksLevel;
import com.islandempires.gameserverservice.model.buildingtype.RawMaterialProductionStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrickWorks extends RawMaterialProductionStructures implements Serializable {
    private List<BricksWorksLevel> bricksWorksLevelList;

    @Override
    protected IslandBuildingEnum getBuildName() {
        return IslandBuildingEnum.BRICK_WORKS;
    }
}
