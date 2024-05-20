package com.islandempires.buildingservice.shared.building;

import com.islandempires.buildingservice.shared.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.shared.buildinglevelspec.CannonCampLevel;
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
public class CannonCamp extends MilitaryStructures implements Serializable {
    private List<CannonCampLevel> cannonCampLevels;


    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return cannonCampLevels.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
