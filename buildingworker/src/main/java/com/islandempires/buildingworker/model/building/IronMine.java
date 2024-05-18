package com.islandempires.buildingworker.model.building;

import com.islandempires.buildingworker.enums.IslandBuildingEnum;
import com.islandempires.buildingworker.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingworker.model.buildinglevelspec.IronMineLevel;
import com.islandempires.buildingworker.model.buildingtype.RawMaterialProductionStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class IronMine extends RawMaterialProductionStructures implements Serializable {
    private List<IronMineLevel> ironMineLevelList;

    protected IronMine() {
        this.islandBuildingEnum = IslandBuildingEnum.IRON_MINE;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return ironMineLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }

    @Override
    public void executeBuildingLogic() {

    }
}
