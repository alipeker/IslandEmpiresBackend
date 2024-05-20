package com.islandempires.buildingworker.shared.building;

import com.islandempires.buildingworker.shared.buildinglevelspec.BarrackLevel;
import com.islandempires.buildingworker.shared.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingworker.shared.buildingtype.MilitaryStructures;
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

    @Override
    public void islandBuildingLevelUpExecution(int nextLvl, String islandId) {

    }
}
