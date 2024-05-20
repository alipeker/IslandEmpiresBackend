package com.islandempires.buildingservice.shared.building;

import com.islandempires.buildingservice.shared.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.shared.buildinglevelspec.RiffleBarrackLevel;
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
public class RiffleBarrack extends MilitaryStructures implements Serializable {
    private List<RiffleBarrackLevel> riffleBarrackLevelList;

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return riffleBarrackLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
