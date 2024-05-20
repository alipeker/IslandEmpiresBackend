package com.islandempires.buildingservice.shared.building;

import com.islandempires.buildingservice.shared.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.shared.buildinglevelspec.HouseLevel;
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
public class Houses extends BasicStructures implements Serializable {
    private List<HouseLevel> houseLevelList;


    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return houseLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
