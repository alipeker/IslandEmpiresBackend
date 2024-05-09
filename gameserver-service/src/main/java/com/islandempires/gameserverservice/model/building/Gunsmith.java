package com.islandempires.gameserverservice.model.building;

import com.islandempires.gameserverservice.enums.IslandBuildingEnum;
import com.islandempires.gameserverservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.gameserverservice.model.buildinglevelspec.GunsmithLevel;
import com.islandempires.gameserverservice.model.buildingtype.MilitaryStructures;
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


    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return gunsmithLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
