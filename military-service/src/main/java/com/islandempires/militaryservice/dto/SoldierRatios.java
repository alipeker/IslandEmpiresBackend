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
}
