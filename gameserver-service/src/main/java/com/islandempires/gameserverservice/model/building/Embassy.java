package com.islandempires.gameserverservice.model.building;

import com.islandempires.gameserverservice.enums.IslandBuildingEnum;
import com.islandempires.gameserverservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.gameserverservice.model.buildinglevelspec.EmbassyLevel;
import com.islandempires.gameserverservice.model.buildingtype.BasicStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Embassy extends BasicStructures implements Serializable {
    private List<EmbassyLevel> embassyLevelList;

    private Integer minLvlForClanCreation;

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return embassyLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
