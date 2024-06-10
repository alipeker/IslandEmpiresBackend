package com.islandempires.militaryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoldierTotalDefenceAgainstSoldierType {
    private BigDecimal infantrymanDefencePoint;
    private BigDecimal riflesDefencePoint;
    private BigDecimal cannonDefencePoint;
    private BigDecimal shipDefencePoint;

    public SoldierTotalDefenceAgainstSoldierType addPoints(SoldierTotalDefenceAgainstSoldierType addTotalDefencePoint) {
        SoldierTotalDefenceAgainstSoldierType sum = new SoldierTotalDefenceAgainstSoldierType();
        sum.setInfantrymanDefencePoint(addTotalDefencePoint.getInfantrymanDefencePoint().add(this.infantrymanDefencePoint));
        sum.setRiflesDefencePoint(addTotalDefencePoint.getRiflesDefencePoint().add(this.riflesDefencePoint));
        sum.setCannonDefencePoint(addTotalDefencePoint.getCannonDefencePoint().add(this.cannonDefencePoint));
        sum.setShipDefencePoint(addTotalDefencePoint.getShipDefencePoint().add(this.shipDefencePoint));
        return sum;
    }
}
