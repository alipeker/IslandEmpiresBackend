package com.islandempires.buildingservice.model.building;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.model.buildinglevelspec.CannonCampLevel;
import com.islandempires.buildingservice.model.buildingtype.MilitaryStructures;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class CannonCamp extends MilitaryStructures implements Serializable {
    private List<CannonCampLevel> cannonCampLevels;

    protected CannonCamp() {
        this.islandBuildingEnum = IslandBuildingEnum.CANNON_CAMP;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return cannonCampLevels.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
