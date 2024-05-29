package com.islandempires.militaryservice.model;

import com.islandempires.militaryservice.dto.SoldierRatios;
import com.islandempires.militaryservice.enums.SoldierSubTypeEnum;
import com.islandempires.militaryservice.model.soldierStats.MilitaryUnits;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@Entity(name = "IslandMilitary")
@AllArgsConstructor
@NoArgsConstructor
public class IslandMilitary {

    @Id
    private String islandId;

    private IslandMilitaryUnits islandMilitaryUnits;

    private List<SupportMilitaryUnits> supportMilitaryUnitsList;

    private int defensePointChangePercent;

    public double calculateIslandTotalDefencePoints(HashMap<SoldierSubTypeEnum, MilitaryUnits> militaryUnits, SoldierRatios soldierRatios) {
        double islandTotalDefencePoint = 0;

        islandTotalDefencePoint += islandMilitaryUnits.calculateTotalDefencePointsAgainstArmyRatio(militaryUnits, soldierRatios);

        for(SupportMilitaryUnits supportMilitaryUnits : this.supportMilitaryUnitsList) {
            islandTotalDefencePoint += supportMilitaryUnits.getIslandMilitaryUnits().calculateTotalDefencePointsAgainstArmyRatio(militaryUnits, soldierRatios);
        }

        return islandTotalDefencePoint * this.defensePointChangePercent;
    }
}
