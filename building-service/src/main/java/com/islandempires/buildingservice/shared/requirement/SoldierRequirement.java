package com.islandempires.buildingservice.shared.requirement;

import com.islandempires.buildingservice.enums.SoldierSubTypeEnum;
import com.islandempires.buildingservice.shared.resources.RawMaterialsAndPopulationCost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoldierRequirement {
    private SoldierSubTypeEnum soldierType;
    private List<BuildingLvlRequirement> buildingLvlRequirementList;
    private RawMaterialsAndPopulationCost rawMaterialsAndPopulationCost;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoldierRequirement that = (SoldierRequirement) o;
        return soldierType == that.soldierType && Objects.equals(buildingLvlRequirementList, that.buildingLvlRequirementList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(soldierType, buildingLvlRequirementList);
    }

    @Override
    public String toString() {
        return "SoldierRequirement{" +
                "soldierType=" + soldierType +
                ", buildingLvlRequirementList=" + buildingLvlRequirementList +
                '}';
    }
}
