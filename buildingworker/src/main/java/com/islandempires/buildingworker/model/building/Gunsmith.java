package com.islandempires.buildingworker.model.building;

import com.islandempires.buildingworker.enums.IslandBuildingEnum;
import com.islandempires.buildingworker.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingworker.model.buildinglevelspec.GunsmithLevel;
import com.islandempires.buildingworker.model.buildingtype.MilitaryStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Gunsmith extends MilitaryStructures implements Serializable {
    private List<GunsmithLevel> gunsmithLevelList;

    protected Gunsmith() {
        this.islandBuildingEnum = IslandBuildingEnum.GUNSMITH;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return gunsmithLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }

    @Override
    public void executeBuildingLogic() {

    }
}
