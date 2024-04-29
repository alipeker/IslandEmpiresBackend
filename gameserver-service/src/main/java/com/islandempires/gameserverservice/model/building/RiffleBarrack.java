package com.islandempires.gameserverservice.model.building;

import com.islandempires.gameserverservice.enums.IslandBuildingEnum;
import com.islandempires.gameserverservice.model.buildinglevelspec.RiffleBarrackLevel;
import com.islandempires.gameserverservice.model.buildingtype.MilitaryStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiffleBarrack extends MilitaryStructures implements Serializable {
    private List<RiffleBarrackLevel> riffleBarrackLevelList;

    @Override
    protected IslandBuildingEnum getBuildName() {
        return IslandBuildingEnum.RIFFLE_BARRACK;
    }
}
