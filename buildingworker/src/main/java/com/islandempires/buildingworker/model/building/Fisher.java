package com.islandempires.buildingworker.model.building;

import com.islandempires.buildingworker.enums.IslandBuildingEnum;
import com.islandempires.buildingworker.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingworker.model.buildinglevelspec.FisherLevel;
import com.islandempires.buildingworker.model.buildingtype.FoodProductionStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Fisher extends FoodProductionStructures implements Serializable {
    private List<FisherLevel> fisherLevelList;

    protected Fisher() {
        this.islandBuildingEnum = IslandBuildingEnum.FISHER;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return fisherLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }

    @Override
    public void executeBuildingLogic() {

    }
}
