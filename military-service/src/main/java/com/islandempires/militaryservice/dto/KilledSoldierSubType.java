package com.islandempires.militaryservice.dto;

import com.islandempires.militaryservice.enums.SoldierSubTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KilledSoldierSubType {
    private BigInteger killedPikemanCount = BigInteger.ZERO;
    private BigInteger killedAxemanCount = BigInteger.ZERO;
    private BigInteger killedArchersCount = BigInteger.ZERO;
    private BigInteger killedSwordsmanCount = BigInteger.ZERO;
    private BigInteger killedLightArmedMusketeerCount = BigInteger.ZERO;
    private BigInteger killedMediumArmedMusketeerCount = BigInteger.ZERO;
    private BigInteger killedHeavyArmedMusketeerCount = BigInteger.ZERO;
    private BigInteger killedCulverinCount = BigInteger.ZERO;
    private BigInteger killedMortarCount = BigInteger.ZERO;
    private BigInteger killedRibaultCount = BigInteger.ZERO;
    private BigInteger killedHolkCount = BigInteger.ZERO;
    private BigInteger killedGunHolkCount = BigInteger.ZERO;
    private BigInteger killedCarrackCount = BigInteger.ZERO;

    public KilledSoldierSubType add(SoldierSubTypeEnum soldierSubTypeEnum, BigInteger count) {
        if(soldierSubTypeEnum.equals(SoldierSubTypeEnum.PIKEMAN)) {
            killedPikemanCount.add(count);
        } else if(soldierSubTypeEnum.equals(SoldierSubTypeEnum.ARCHER)) {
            killedAxemanCount.add(count);
        } else if(soldierSubTypeEnum.equals(SoldierSubTypeEnum.AXEMAN)) {
            killedArchersCount.add(count);
        } else if(soldierSubTypeEnum.equals(SoldierSubTypeEnum.SWORDSMAN)) {
            killedSwordsmanCount.add(count);
        } else if(soldierSubTypeEnum.equals(SoldierSubTypeEnum.LIGHT_ARMED_MUSKETEER)) {
            killedLightArmedMusketeerCount.add(count);
        } else if(soldierSubTypeEnum.equals(SoldierSubTypeEnum.MEDIUM_ARMED_MUSKETEER)) {
            killedMediumArmedMusketeerCount.add(count);
        } else if(soldierSubTypeEnum.equals(SoldierSubTypeEnum.HEAVY_ARMED_MUSKETEER)) {
            killedHeavyArmedMusketeerCount.add(count);
        } else if(soldierSubTypeEnum.equals(SoldierSubTypeEnum.CULVERIN)) {
            killedCulverinCount.add(count);
        } else if(soldierSubTypeEnum.equals(SoldierSubTypeEnum.MORTAR)) {
            killedMortarCount.add(count);
        } else if(soldierSubTypeEnum.equals(SoldierSubTypeEnum.RIBAULT)) {
            killedRibaultCount.add(count);
        } else if(soldierSubTypeEnum.equals(SoldierSubTypeEnum.HOLK)) {
            killedHolkCount.add(count);
        } else if(soldierSubTypeEnum.equals(SoldierSubTypeEnum.GUN_HOLK)) {
            killedGunHolkCount.add(count);
        } else if(soldierSubTypeEnum.equals(SoldierSubTypeEnum.CARRACK)) {
            killedCarrackCount.add(count);
        }
        return this;
    }
}
