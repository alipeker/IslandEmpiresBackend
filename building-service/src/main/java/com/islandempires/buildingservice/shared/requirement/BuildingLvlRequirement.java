package com.islandempires.buildingservice.shared.requirement;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildingLvlRequirement {
    private IslandBuildingEnum islandBuildingEnum;

    private int buildingLvl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuildingLvlRequirement that = (BuildingLvlRequirement) o;
        return buildingLvl == that.buildingLvl && islandBuildingEnum == that.islandBuildingEnum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(islandBuildingEnum, buildingLvl);
    }

    @Override
    public String toString() {
        return "BuildingLvlRequirement{" +
                "islandBuildingEnum=" + islandBuildingEnum +
                ", buildingLvl=" + buildingLvl +
                '}';
    }
}
