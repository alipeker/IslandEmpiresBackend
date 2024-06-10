package com.islandempires.militaryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoldierTotalDefenceAgainstSoldierType {
    private BigDecimal infantrymanDefencePoint = BigDecimal.ZERO;
    private BigDecimal riflesDefencePoint = BigDecimal.ZERO;
    private BigDecimal cannonDefencePoint = BigDecimal.ZERO;
    private BigDecimal shipDefencePoint = BigDecimal.ZERO;

    public SoldierTotalDefenceAgainstSoldierType addPoints(SoldierTotalDefenceAgainstSoldierType addTotalDefencePoint) {
        this.setInfantrymanDefencePoint(addTotalDefencePoint.getInfantrymanDefencePoint().add(this.infantrymanDefencePoint));
        this.setRiflesDefencePoint(addTotalDefencePoint.getRiflesDefencePoint().add(this.riflesDefencePoint));
        this.setCannonDefencePoint(addTotalDefencePoint.getCannonDefencePoint().add(this.cannonDefencePoint));
        this.setShipDefencePoint(addTotalDefencePoint.getShipDefencePoint().add(this.shipDefencePoint));
        return this;
    }

    public SoldierTotalDefenceAgainstSoldierType addListPoints(List<SoldierTotalDefenceAgainstSoldierType> addTotalDefencePoint) {
        addTotalDefencePoint.stream().forEach(defencePoint -> {
            this.setInfantrymanDefencePoint(this.getInfantrymanDefencePoint().add(defencePoint.getInfantrymanDefencePoint()));
            this.setRiflesDefencePoint(this.getRiflesDefencePoint().add(defencePoint.getRiflesDefencePoint()));
            this.setCannonDefencePoint(this.getCannonDefencePoint().add(defencePoint.getCannonDefencePoint()));
            this.setShipDefencePoint(this.getShipDefencePoint().add(defencePoint.getShipDefencePoint()));
        });
        return this;
    }

    public SoldierTotalDefenceAgainstSoldierType divideAllValues(BigDecimal divideValue) {
        this.setInfantrymanDefencePoint(infantrymanDefencePoint.divide(divideValue, 10, RoundingMode.HALF_UP));
        this.setRiflesDefencePoint(riflesDefencePoint.divide(divideValue, 10, RoundingMode.HALF_UP));
        this.setCannonDefencePoint(cannonDefencePoint.divide(divideValue, 10, RoundingMode.HALF_UP));
        this.setShipDefencePoint(shipDefencePoint.divide(divideValue, 10, RoundingMode.HALF_UP));
        return this;
    }
}
