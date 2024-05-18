package com.islandempires.buildingworker.model.building;

import com.islandempires.buildingworker.enums.IslandBuildingEnum;
import com.islandempires.buildingworker.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingworker.model.buildinglevelspec.CannonCampLevel;
import com.islandempires.buildingworker.model.buildingtype.MilitaryStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

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

    @Override
    public void executeBuildingLogic() {

    }
}
