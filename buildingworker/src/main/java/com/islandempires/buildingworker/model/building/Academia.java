package com.islandempires.buildingworker.model.building;

import com.islandempires.buildingworker.enums.IslandBuildingEnum;
import com.islandempires.buildingworker.model.buildinglevelspec.AcademiaLevel;
import com.islandempires.buildingworker.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingworker.model.buildingtype.BasicStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/*
* This building is for researching new troops. When the user levels up, they can research new troops.
* With academia level user can have research point
* */
@Data
@AllArgsConstructor
public class Academia extends BasicStructures implements Serializable {
    private List<AcademiaLevel> academiaLevelList;

    protected Academia() {
        this.islandBuildingEnum = IslandBuildingEnum.ACADEMIA;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return academiaLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }

    @Override
    public void executeBuildingLogic() {

    }
}
