package com.islandempires.buildingworker.model.building;

import com.islandempires.buildingworker.enums.IslandBuildingEnum;
import com.islandempires.buildingworker.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingworker.model.buildinglevelspec.HouseLevel;
import com.islandempires.buildingworker.model.buildingtype.BasicStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Houses extends BasicStructures implements Serializable {
    private List<HouseLevel> houseLevelList;

    protected Houses() {
        this.islandBuildingEnum = IslandBuildingEnum.HOUSES;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return houseLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }

    @Override
    public void executeBuildingLogic() {

    }
}
