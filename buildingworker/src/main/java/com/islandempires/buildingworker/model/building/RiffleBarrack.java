package com.islandempires.buildingworker.model.building;

import com.islandempires.buildingworker.enums.IslandBuildingEnum;
import com.islandempires.buildingworker.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingworker.model.buildinglevelspec.RiffleBarrackLevel;
import com.islandempires.buildingworker.model.buildingtype.MilitaryStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class RiffleBarrack extends MilitaryStructures implements Serializable {
    private List<RiffleBarrackLevel> riffleBarrackLevelList;

    protected RiffleBarrack() {
        this.islandBuildingEnum = IslandBuildingEnum.RIFFLE_BARRACK;
    }

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return riffleBarrackLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }

    @Override
    public void executeBuildingLogic() {

    }
}
