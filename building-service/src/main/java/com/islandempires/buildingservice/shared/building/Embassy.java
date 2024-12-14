package com.islandempires.buildingservice.shared.building;

import com.islandempires.buildingservice.shared.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.shared.buildinglevelspec.EmbassyLevel;
import com.islandempires.buildingservice.shared.buildingtype.BasicStructures;
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

    private int minLvlForClanCreation;

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return embassyLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
