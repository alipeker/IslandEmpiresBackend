package com.islandempires.buildingservice.shared.building;

import com.islandempires.buildingservice.shared.buildinglevelspec.BarrackLevel;
import com.islandempires.buildingservice.shared.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.shared.buildingtype.MilitaryStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Barrack extends MilitaryStructures implements Serializable {
    private List<BarrackLevel> barrackLevelList;

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return barrackLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
