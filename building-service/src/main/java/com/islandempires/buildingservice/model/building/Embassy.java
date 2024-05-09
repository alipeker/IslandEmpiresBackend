package com.islandempires.buildingservice.model.building;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.model.buildinglevelspec.EmbassyLevel;
import com.islandempires.buildingservice.model.buildingtype.BasicStructures;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Embassy extends BasicStructures implements Serializable {
    private List<EmbassyLevel> embassyLevelList;

    private Integer minLvlForClanCreation;

    protected Embassy() {
        this.islandBuildingEnum = IslandBuildingEnum.EMBASSY;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return embassyLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
