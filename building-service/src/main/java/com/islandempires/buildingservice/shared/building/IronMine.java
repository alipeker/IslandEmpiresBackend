package com.islandempires.buildingservice.shared.building;

import com.islandempires.buildingservice.shared.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.shared.buildinglevelspec.IronMineLevel;
import com.islandempires.buildingservice.shared.buildingtype.RawMaterialProductionStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IronMine extends RawMaterialProductionStructures implements Serializable {
    private List<IronMineLevel> ironMineLevelList;

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return ironMineLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
