package com.islandempires.militaryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoldierRatios {
    private double infantrymanToTotalSoldiersRatio;
    private double riflesToTotalSoldiersRatio;
    private double cannonToTotalSoldiersRatio;
    private double shipsToTotalSoldiersRatio;

    public SoldierRatios addSoldierRatio(SoldierRatios soldierRatios) {
        infantrymanToTotalSoldiersRatio += soldierRatios.getInfantrymanToTotalSoldiersRatio();
        riflesToTotalSoldiersRatio += soldierRatios.getRiflesToTotalSoldiersRatio();
        cannonToTotalSoldiersRatio += soldierRatios.getCannonToTotalSoldiersRatio();
        shipsToTotalSoldiersRatio += soldierRatios.getShipsToTotalSoldiersRatio();
        return this;
    }

    public boolean isAllValueZero() {
        return 0.0 == infantrymanToTotalSoldiersRatio &&  0 == riflesToTotalSoldiersRatio &&
                0.0 == cannonToTotalSoldiersRatio && 0 == shipsToTotalSoldiersRatio;
    }

    public SoldierRatios setAllValueAsOne() {
        infantrymanToTotalSoldiersRatio = 1.0;
        riflesToTotalSoldiersRatio = 1.0;
        cannonToTotalSoldiersRatio = 1.0;
        shipsToTotalSoldiersRatio = 1.0;
        return this;
    }
}
