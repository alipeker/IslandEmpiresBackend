package com.islandempires.buildingworker.shared.building;

import com.islandempires.buildingworker.shared.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingworker.shared.buildinglevelspec.EmbassyLevel;
import com.islandempires.buildingworker.shared.buildingtype.BasicStructures;
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

    @Override
    public void islandBuildingLevelUpExecution(int nextLvl, String islandId) {

    }
}
