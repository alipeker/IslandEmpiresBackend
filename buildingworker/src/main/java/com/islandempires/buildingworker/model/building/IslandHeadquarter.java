package com.islandempires.buildingworker.model.building;

import com.islandempires.buildingworker.enums.IslandBuildingEnum;
import com.islandempires.buildingworker.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingworker.model.buildinglevelspec.IslandHeadquarterLevel;
import com.islandempires.buildingworker.model.buildingtype.BasicStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class IslandHeadquarter extends BasicStructures implements Serializable {
    private List<IslandHeadquarterLevel> islandHeadquarterLevelList;

    protected IslandHeadquarter() {
        this.islandBuildingEnum = IslandBuildingEnum.ISLAND_HEADQUARTERS;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return islandHeadquarterLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }

    @Override
    public void executeBuildingLogic() {

    }
}
