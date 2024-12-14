package com.islandempires.buildingservice.shared.building;

import com.islandempires.buildingservice.shared.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.shared.buildinglevelspec.GunsmithLevel;
import com.islandempires.buildingservice.shared.buildingtype.MilitaryStructures;
import com.islandempires.buildingservice.shared.requirement.SoldierRequirement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Gunsmith extends MilitaryStructures implements Serializable {
    private List<GunsmithLevel> gunsmithLevelList;

    private List<SoldierRequirement> requirements;

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return gunsmithLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
