package com.islandempires.buildingservice.model.building;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.buildinglevelspec.CannonCampLevel;
import com.islandempires.buildingservice.model.buildingtype.MilitaryStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CannonCamp extends MilitaryStructures implements Serializable {
    private List<CannonCampLevel> cannonCampLevels;

    @Override
    protected IslandBuildingEnum getBuildName() {
        return IslandBuildingEnum.CANNON_CAMP;
    }
}
