package com.islandempires.militaryservice.model;

import com.islandempires.militaryservice.dto.SoldierRatios;
import com.islandempires.militaryservice.enums.SoldierSubTypeEnum;
import com.islandempires.militaryservice.enums.SoldierTypeEnum;
import com.islandempires.militaryservice.model.soldierStats.MilitaryUnits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IslandMilitaryUnits {
    private int pikemanCount;
    private int axemanCount;
    private int archerCount;
    private int swordsmanCount;
    private int lightArmedMusketeerCount;
    private int mediumArmedMusketeerCount;
    private int heavyArmedMusketeerCount;
    private int culverinCount;
    private int mortarCount;
    private int ribaultCount;
    private int holkCount;
    private int gunHolkCount;
    private int carrackCount;

    public int getInfantrymanNumber() {
        return swordsmanCount + pikemanCount + archerCount + axemanCount;
    }

    public int getRifleNumber() {
        return lightArmedMusketeerCount + mediumArmedMusketeerCount + heavyArmedMusketeerCount;
    }

    public int getCannonNumber() {
        return culverinCount + mortarCount + ribaultCount;
    }

    public int getShipNumber() {
        return holkCount + gunHolkCount + carrackCount;
    }

    public int getTotalSoldiers() {
        return getInfantrymanNumber() + getRifleNumber() + getCannonNumber() + getShipNumber();
    }

    public SoldierRatios calculateRatioPerEachSoldierType() {
        int totalSoldiers = getTotalSoldiers();
        double infantrymanRatio = (double) getInfantrymanNumber() / totalSoldiers;
        double rifleRatio = (double) getRifleNumber() / totalSoldiers;
        double cannonRatio = (double) getCannonNumber() / totalSoldiers;
        double shipRatio = (double) getShipNumber() / totalSoldiers;

        return new SoldierRatios(infantrymanRatio, rifleRatio, cannonRatio, shipRatio);
    }


    public double getAverageDefencePointAgainstArmyRatioBySubSoldierType(Map<SoldierTypeEnum, Integer> soldierDefencePoints, SoldierRatios soldierRatios) {
        return ((soldierDefencePoints.get(SoldierTypeEnum.INFANTRYMAN) * soldierRatios.getInfantrymanToTotalSoldiersRatio()) +
                (soldierDefencePoints.get(SoldierTypeEnum.RIFLE) * soldierRatios.getRiflesToTotalSoldiersRatio()) +
                (soldierDefencePoints.get(SoldierTypeEnum.CANNON) * soldierRatios.getCannonToTotalSoldiersRatio()) +
                (soldierDefencePoints.get(SoldierTypeEnum.SHIP) * soldierRatios.getShipsToTotalSoldiersRatio())) / 4;
    }

    private double addCalculatedDefensePoints(SoldierSubTypeEnum soldierType, int count, SoldierRatios soldierRatios, Map<SoldierSubTypeEnum, MilitaryUnits> militaryUnits) {
        MilitaryUnits soldier = militaryUnits.get(soldierType);
        if(soldier == null || soldierRatios == null) {
            return 0;
        }
        double averageDefencePoint = this.getAverageDefencePointAgainstArmyRatioBySubSoldierType(soldier.getDefensePoints(), soldierRatios);
        return averageDefencePoint * count;
    }

    public double calculateTotalDefencePointsAgainstArmyRatio(HashMap<SoldierSubTypeEnum, MilitaryUnits> militaryUnits, SoldierRatios soldierRatios) {
        double totalDefencePoint = 0;
        totalDefencePoint += addCalculatedDefensePoints(SoldierSubTypeEnum.SWORDSMAN, swordsmanCount, soldierRatios, militaryUnits);
        totalDefencePoint += addCalculatedDefensePoints(SoldierSubTypeEnum.PIKEMAN, pikemanCount, soldierRatios, militaryUnits);
        totalDefencePoint += addCalculatedDefensePoints(SoldierSubTypeEnum.AXEMAN, axemanCount, soldierRatios, militaryUnits);
        totalDefencePoint += addCalculatedDefensePoints(SoldierSubTypeEnum.ARCHER, archerCount, soldierRatios, militaryUnits);
        totalDefencePoint += addCalculatedDefensePoints(SoldierSubTypeEnum.LIGHT_ARMED_MUSKETEER, lightArmedMusketeerCount, soldierRatios, militaryUnits);
        totalDefencePoint += addCalculatedDefensePoints(SoldierSubTypeEnum.MEDIUM_ARMED_MUSKETEER, mediumArmedMusketeerCount, soldierRatios, militaryUnits);
        totalDefencePoint += addCalculatedDefensePoints(SoldierSubTypeEnum.HEAVY_ARMED_MUSKETEER, heavyArmedMusketeerCount, soldierRatios, militaryUnits);
        totalDefencePoint += addCalculatedDefensePoints(SoldierSubTypeEnum.CULVERIN, culverinCount, soldierRatios, militaryUnits);
        totalDefencePoint += addCalculatedDefensePoints(SoldierSubTypeEnum.MORTAR, mortarCount, soldierRatios, militaryUnits);
        totalDefencePoint += addCalculatedDefensePoints(SoldierSubTypeEnum.RIBAULT, ribaultCount, soldierRatios, militaryUnits);
        totalDefencePoint += addCalculatedDefensePoints(SoldierSubTypeEnum.HOLK, holkCount, soldierRatios, militaryUnits);
        totalDefencePoint += addCalculatedDefensePoints(SoldierSubTypeEnum.GUN_HOLK, gunHolkCount, soldierRatios, militaryUnits);
        totalDefencePoint += addCalculatedDefensePoints(SoldierSubTypeEnum.CARRACK, carrackCount, soldierRatios, militaryUnits);
        return totalDefencePoint;
    }

    private double addCalculatedAttackPoints(SoldierSubTypeEnum soldierType, int count, Map<SoldierSubTypeEnum, MilitaryUnits> militaryUnits) {
        MilitaryUnits soldier = militaryUnits.get(soldierType);
        return soldier != null ? soldier.getAttackPoint() * count : 0;
    }

    public double calculateTotalAttackPoints(HashMap<SoldierSubTypeEnum, MilitaryUnits> militaryUnits) {
        double totalAttackPoint = 0;
        totalAttackPoint += addCalculatedAttackPoints(SoldierSubTypeEnum.SWORDSMAN, swordsmanCount, militaryUnits);
        totalAttackPoint += addCalculatedAttackPoints(SoldierSubTypeEnum.PIKEMAN, pikemanCount, militaryUnits);
        totalAttackPoint += addCalculatedAttackPoints(SoldierSubTypeEnum.AXEMAN, axemanCount, militaryUnits);
        totalAttackPoint += addCalculatedAttackPoints(SoldierSubTypeEnum.ARCHER, archerCount, militaryUnits);
        totalAttackPoint += addCalculatedAttackPoints(SoldierSubTypeEnum.LIGHT_ARMED_MUSKETEER, lightArmedMusketeerCount, militaryUnits);
        totalAttackPoint += addCalculatedAttackPoints(SoldierSubTypeEnum.MEDIUM_ARMED_MUSKETEER, mediumArmedMusketeerCount, militaryUnits);
        totalAttackPoint += addCalculatedAttackPoints(SoldierSubTypeEnum.HEAVY_ARMED_MUSKETEER, heavyArmedMusketeerCount, militaryUnits);
        totalAttackPoint += addCalculatedAttackPoints(SoldierSubTypeEnum.CULVERIN, culverinCount, militaryUnits);
        totalAttackPoint += addCalculatedAttackPoints(SoldierSubTypeEnum.MORTAR, mortarCount, militaryUnits);
        totalAttackPoint += addCalculatedAttackPoints(SoldierSubTypeEnum.RIBAULT, ribaultCount, militaryUnits);
        totalAttackPoint += addCalculatedAttackPoints(SoldierSubTypeEnum.HOLK, holkCount, militaryUnits);
        totalAttackPoint += addCalculatedAttackPoints(SoldierSubTypeEnum.GUN_HOLK, gunHolkCount, militaryUnits);
        totalAttackPoint += addCalculatedAttackPoints(SoldierSubTypeEnum.CARRACK, carrackCount, militaryUnits);
        return totalAttackPoint;
    }

}
