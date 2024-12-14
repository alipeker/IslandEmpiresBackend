package com.islandempires.militaryservice.dto;

import com.islandempires.militaryservice.enums.SoldierSubTypeEnum;
import com.islandempires.militaryservice.model.soldier.Soldier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
public class TotalAttackPointForKillSoldierMainType {

    private BigDecimal totalAttackPointForKillInfantryman = BigDecimal.ZERO;

    private BigDecimal totalAttackPointForKillRifle = BigDecimal.ZERO;

    private BigDecimal totalAttackPointForKillCannon = BigDecimal.ZERO;

    private BigDecimal totalAttackPointForKillShip = BigDecimal.ZERO;

    private HashMap<SoldierSubTypeEnum, BigDecimal> totalAttackPointForKillInfantrymanSubTypes = new HashMap();

    private HashMap<SoldierSubTypeEnum, BigDecimal> totalAttackPointForKillRifleSubTypes = new HashMap();

    private HashMap<SoldierSubTypeEnum, BigDecimal> totalAttackPointForKillCannonSubTypes = new HashMap();

    private HashMap<SoldierSubTypeEnum, BigDecimal> totalAttackPointForKillShipSubTypes = new HashMap();

    public TotalAttackPointForKillSoldierMainType() {
        totalAttackPointForKillInfantrymanSubTypes.put(SoldierSubTypeEnum.PIKEMAN, BigDecimal.ZERO);
        totalAttackPointForKillInfantrymanSubTypes.put(SoldierSubTypeEnum.AXEMAN, BigDecimal.ZERO);
        totalAttackPointForKillInfantrymanSubTypes.put(SoldierSubTypeEnum.ARCHER, BigDecimal.ZERO);
        totalAttackPointForKillInfantrymanSubTypes.put(SoldierSubTypeEnum.SWORDSMAN, BigDecimal.ZERO);
        totalAttackPointForKillInfantrymanSubTypes.put(SoldierSubTypeEnum.MISSIONARY, BigDecimal.ZERO);


        totalAttackPointForKillRifleSubTypes.put(SoldierSubTypeEnum.LIGHT_ARMED_MUSKETEER, BigDecimal.ZERO);
        totalAttackPointForKillRifleSubTypes.put(SoldierSubTypeEnum.MEDIUM_ARMED_MUSKETEER, BigDecimal.ZERO);
        totalAttackPointForKillRifleSubTypes.put(SoldierSubTypeEnum.HEAVY_ARMED_MUSKETEER, BigDecimal.ZERO);

        totalAttackPointForKillCannonSubTypes.put(SoldierSubTypeEnum.CULVERIN, BigDecimal.ZERO);
        totalAttackPointForKillCannonSubTypes.put(SoldierSubTypeEnum.MORTAR, BigDecimal.ZERO);
        totalAttackPointForKillCannonSubTypes.put(SoldierSubTypeEnum.RIBAULT, BigDecimal.ZERO);

        totalAttackPointForKillShipSubTypes.put(SoldierSubTypeEnum.HOLK, BigDecimal.ZERO);
        totalAttackPointForKillShipSubTypes.put(SoldierSubTypeEnum.GUN_HOLK, BigDecimal.ZERO);
        totalAttackPointForKillShipSubTypes.put(SoldierSubTypeEnum.CARRACK, BigDecimal.ZERO);
    }

    public void divideAll(BigDecimal denominator) {
        totalAttackPointForKillInfantrymanSubTypes.put(SoldierSubTypeEnum.PIKEMAN, totalAttackPointForKillInfantrymanSubTypes.get(SoldierSubTypeEnum.PIKEMAN).divide(denominator, 10, RoundingMode.HALF_UP));
        totalAttackPointForKillInfantrymanSubTypes.put(SoldierSubTypeEnum.AXEMAN, totalAttackPointForKillInfantrymanSubTypes.get(SoldierSubTypeEnum.AXEMAN).divide(denominator, 10, RoundingMode.HALF_UP));
        totalAttackPointForKillInfantrymanSubTypes.put(SoldierSubTypeEnum.ARCHER, totalAttackPointForKillInfantrymanSubTypes.get(SoldierSubTypeEnum.ARCHER).divide(denominator, 10, RoundingMode.HALF_UP));
        totalAttackPointForKillInfantrymanSubTypes.put(SoldierSubTypeEnum.SWORDSMAN, totalAttackPointForKillInfantrymanSubTypes.get(SoldierSubTypeEnum.SWORDSMAN).divide(denominator, 10, RoundingMode.HALF_UP));
        totalAttackPointForKillInfantrymanSubTypes.put(SoldierSubTypeEnum.MISSIONARY, totalAttackPointForKillInfantrymanSubTypes.get(SoldierSubTypeEnum.MISSIONARY).divide(denominator, 10, RoundingMode.HALF_UP));
        totalAttackPointForKillInfantryman = totalAttackPointForKillInfantryman.divide(denominator, 10, RoundingMode.HALF_UP);

        totalAttackPointForKillRifleSubTypes.put(SoldierSubTypeEnum.LIGHT_ARMED_MUSKETEER, totalAttackPointForKillRifleSubTypes.get(SoldierSubTypeEnum.LIGHT_ARMED_MUSKETEER).divide(denominator, 10, RoundingMode.HALF_UP));
        totalAttackPointForKillRifleSubTypes.put(SoldierSubTypeEnum.MEDIUM_ARMED_MUSKETEER, totalAttackPointForKillRifleSubTypes.get(SoldierSubTypeEnum.MEDIUM_ARMED_MUSKETEER).divide(denominator, 10, RoundingMode.HALF_UP));
        totalAttackPointForKillRifleSubTypes.put(SoldierSubTypeEnum.HEAVY_ARMED_MUSKETEER, totalAttackPointForKillRifleSubTypes.get(SoldierSubTypeEnum.HEAVY_ARMED_MUSKETEER).divide(denominator, 10, RoundingMode.HALF_UP));
        totalAttackPointForKillRifle = totalAttackPointForKillRifle.divide(denominator, 10, RoundingMode.HALF_UP);

        totalAttackPointForKillCannonSubTypes.put(SoldierSubTypeEnum.CULVERIN, totalAttackPointForKillCannonSubTypes.get(SoldierSubTypeEnum.CULVERIN).divide(denominator, 10, RoundingMode.HALF_UP));
        totalAttackPointForKillCannonSubTypes.put(SoldierSubTypeEnum.MORTAR, totalAttackPointForKillCannonSubTypes.get(SoldierSubTypeEnum.MORTAR).divide(denominator, 10, RoundingMode.HALF_UP));
        totalAttackPointForKillCannonSubTypes.put(SoldierSubTypeEnum.RIBAULT, totalAttackPointForKillCannonSubTypes.get(SoldierSubTypeEnum.RIBAULT).divide(denominator, 10, RoundingMode.HALF_UP));
        totalAttackPointForKillCannon = totalAttackPointForKillCannon.divide(denominator, 10, RoundingMode.HALF_UP);

        totalAttackPointForKillShipSubTypes.put(SoldierSubTypeEnum.HOLK, totalAttackPointForKillShipSubTypes.get(SoldierSubTypeEnum.HOLK).divide(denominator, 10, RoundingMode.HALF_UP));
        totalAttackPointForKillShipSubTypes.put(SoldierSubTypeEnum.GUN_HOLK, totalAttackPointForKillShipSubTypes.get(SoldierSubTypeEnum.GUN_HOLK).divide(denominator, 10, RoundingMode.HALF_UP));
        totalAttackPointForKillShipSubTypes.put(SoldierSubTypeEnum.CARRACK, totalAttackPointForKillShipSubTypes.get(SoldierSubTypeEnum.CARRACK).divide(denominator, 10, RoundingMode.HALF_UP));
        totalAttackPointForKillShip = totalAttackPointForKillShip.divide(denominator, 10, RoundingMode.HALF_UP);
    }

    public TotalAttackPointForKillSoldierMainType multiplyAll(SoldierTotalDefenceAgainstSoldierType soldierTotalDefenceAgainstSoldierType) {
        totalAttackPointForKillInfantrymanSubTypes.put(SoldierSubTypeEnum.PIKEMAN, totalAttackPointForKillInfantrymanSubTypes.get(SoldierSubTypeEnum.PIKEMAN)
                .multiply(soldierTotalDefenceAgainstSoldierType.getInfantrymanDefencePoint()));
        totalAttackPointForKillInfantrymanSubTypes.put(SoldierSubTypeEnum.AXEMAN, totalAttackPointForKillInfantrymanSubTypes.get(SoldierSubTypeEnum.AXEMAN)
                .multiply(soldierTotalDefenceAgainstSoldierType.getInfantrymanDefencePoint()));
        totalAttackPointForKillInfantrymanSubTypes.put(SoldierSubTypeEnum.ARCHER, totalAttackPointForKillInfantrymanSubTypes.get(SoldierSubTypeEnum.ARCHER)
                .multiply(soldierTotalDefenceAgainstSoldierType.getInfantrymanDefencePoint()));
        totalAttackPointForKillInfantrymanSubTypes.put(SoldierSubTypeEnum.SWORDSMAN, totalAttackPointForKillInfantrymanSubTypes.get(SoldierSubTypeEnum.SWORDSMAN)
                .multiply(soldierTotalDefenceAgainstSoldierType.getInfantrymanDefencePoint()));
        totalAttackPointForKillInfantrymanSubTypes.put(SoldierSubTypeEnum.MISSIONARY, totalAttackPointForKillInfantrymanSubTypes.get(SoldierSubTypeEnum.MISSIONARY)
                .multiply(soldierTotalDefenceAgainstSoldierType.getInfantrymanDefencePoint()));
        totalAttackPointForKillInfantryman = totalAttackPointForKillInfantryman.multiply(soldierTotalDefenceAgainstSoldierType.getInfantrymanDefencePoint());

        totalAttackPointForKillRifleSubTypes.put(SoldierSubTypeEnum.LIGHT_ARMED_MUSKETEER, totalAttackPointForKillRifleSubTypes.get(SoldierSubTypeEnum.LIGHT_ARMED_MUSKETEER)
                .multiply(soldierTotalDefenceAgainstSoldierType.getRiflesDefencePoint()));
        totalAttackPointForKillRifleSubTypes.put(SoldierSubTypeEnum.MEDIUM_ARMED_MUSKETEER, totalAttackPointForKillRifleSubTypes.get(SoldierSubTypeEnum.MEDIUM_ARMED_MUSKETEER)
                .multiply(soldierTotalDefenceAgainstSoldierType.getRiflesDefencePoint()));
        totalAttackPointForKillRifleSubTypes.put(SoldierSubTypeEnum.HEAVY_ARMED_MUSKETEER, totalAttackPointForKillRifleSubTypes.get(SoldierSubTypeEnum.HEAVY_ARMED_MUSKETEER)
                .multiply(soldierTotalDefenceAgainstSoldierType.getRiflesDefencePoint()));
        totalAttackPointForKillRifle = totalAttackPointForKillRifle.multiply(soldierTotalDefenceAgainstSoldierType.getRiflesDefencePoint());

        totalAttackPointForKillCannonSubTypes.put(SoldierSubTypeEnum.CULVERIN, totalAttackPointForKillCannonSubTypes.get(SoldierSubTypeEnum.CULVERIN)
                .multiply(soldierTotalDefenceAgainstSoldierType.getCannonDefencePoint()));
        totalAttackPointForKillCannonSubTypes.put(SoldierSubTypeEnum.MORTAR, totalAttackPointForKillCannonSubTypes.get(SoldierSubTypeEnum.MORTAR)
                .multiply(soldierTotalDefenceAgainstSoldierType.getCannonDefencePoint()));
        totalAttackPointForKillCannonSubTypes.put(SoldierSubTypeEnum.RIBAULT, totalAttackPointForKillCannonSubTypes.get(SoldierSubTypeEnum.RIBAULT)
                .multiply(soldierTotalDefenceAgainstSoldierType.getCannonDefencePoint()));
        totalAttackPointForKillCannon = totalAttackPointForKillCannon.multiply(soldierTotalDefenceAgainstSoldierType.getCannonDefencePoint());

        totalAttackPointForKillShipSubTypes.put(SoldierSubTypeEnum.HOLK, totalAttackPointForKillShipSubTypes.get(SoldierSubTypeEnum.HOLK)
                .multiply(soldierTotalDefenceAgainstSoldierType.getShipDefencePoint()));
        totalAttackPointForKillShipSubTypes.put(SoldierSubTypeEnum.GUN_HOLK, totalAttackPointForKillShipSubTypes.get(SoldierSubTypeEnum.GUN_HOLK)
                .multiply(soldierTotalDefenceAgainstSoldierType.getShipDefencePoint()));
        totalAttackPointForKillShipSubTypes.put(SoldierSubTypeEnum.CARRACK, totalAttackPointForKillShipSubTypes.get(SoldierSubTypeEnum.CARRACK)
                .multiply(soldierTotalDefenceAgainstSoldierType.getShipDefencePoint()));
        totalAttackPointForKillShip = totalAttackPointForKillShip.multiply(soldierTotalDefenceAgainstSoldierType.getShipDefencePoint());
        return this;
    }

    public void calculateTotalAttackPointForKillInfantrymanSubTypes(List<Soldier> soldierAttackPoints, double defenceMultiplier) {
        BigInteger sum = soldierAttackPoints.stream()
                .map(soldier -> soldier.calculateTotalAttackPoint(defenceMultiplier))
                .reduce(BigInteger.ZERO, BigInteger::add);

        if(sum.compareTo(BigInteger.ZERO) == 0) {
            return;
        }

        soldierAttackPoints.stream().forEach(soldier -> {
                BigDecimal soldierTotalAttackPoint = new BigDecimal(soldier.calculateTotalAttackPoint(defenceMultiplier)).divide(new BigDecimal(sum), 10, RoundingMode.HALF_UP).multiply(totalAttackPointForKillInfantryman);
                totalAttackPointForKillInfantrymanSubTypes.put(SoldierSubTypeEnum.valueOf(soldier.getSoldierBaseInfo().getSoldierSubTypeName()), soldierTotalAttackPoint);
        });
    }

    public void calculateTotalAttackPointForKillRifleSubTypes(List<Soldier> soldierAttackPoints, double defenceMultiplier) {
        BigInteger sum = soldierAttackPoints.stream()
                .map(soldier -> soldier.calculateTotalAttackPoint(defenceMultiplier))
                .reduce(BigInteger.ZERO, BigInteger::add);

        if(sum.compareTo(BigInteger.ZERO) == 0) {
            return;
        }

        soldierAttackPoints.stream().forEach(soldier -> {
            BigDecimal soldierTotalAttackPoint = new BigDecimal(soldier.calculateTotalAttackPoint(defenceMultiplier)).divide(new BigDecimal(sum), 10, RoundingMode.HALF_UP).multiply(totalAttackPointForKillRifle);
            totalAttackPointForKillRifleSubTypes.put(SoldierSubTypeEnum.valueOf(soldier.getSoldierBaseInfo().getSoldierSubTypeName()), soldierTotalAttackPoint);
        });
    }

    public void calculateTotalAttackPointForKillCannonSubTypes(List<Soldier> soldierAttackPoints, double defenceMultiplier) {
        BigInteger sum = soldierAttackPoints.stream()
                .map(soldier -> soldier.calculateTotalAttackPoint(defenceMultiplier))
                .reduce(BigInteger.ZERO, BigInteger::add);

        if(sum.compareTo(BigInteger.ZERO) == 0) {
            return;
        }

        soldierAttackPoints.stream().forEach(soldier -> {
            BigDecimal soldierTotalAttackPoint = new BigDecimal(soldier.calculateTotalAttackPoint(defenceMultiplier)).divide(new BigDecimal(sum), 10, RoundingMode.HALF_UP).multiply(totalAttackPointForKillCannon);
            totalAttackPointForKillCannonSubTypes.put(SoldierSubTypeEnum.valueOf(soldier.getSoldierBaseInfo().getSoldierSubTypeName()), soldierTotalAttackPoint);
        });
    }

    public void calculateTotalAttackPointForKillShipSubTypes(List<Soldier> soldierAttackPoints, double defenceMultiplier) {
        BigInteger sum = soldierAttackPoints.stream()
                .map(soldier -> soldier.calculateTotalAttackPoint(defenceMultiplier))
                .reduce(BigInteger.ZERO, BigInteger::add);

        if(sum.compareTo(BigInteger.ZERO) == 0) {
            return;
        }

        soldierAttackPoints.stream().forEach(soldier -> {
            BigDecimal soldierTotalAttackPoint = new BigDecimal(soldier.calculateTotalAttackPoint(defenceMultiplier)).divide(new BigDecimal(sum), 10, RoundingMode.HALF_UP).multiply(totalAttackPointForKillShip);
            totalAttackPointForKillShipSubTypes.put(SoldierSubTypeEnum.valueOf(soldier.getSoldierBaseInfo().getSoldierSubTypeName()), soldierTotalAttackPoint);
        });
    }

    public TotalAttackPointForKillSoldierMainType subtractInfantryManSubtype(SoldierSubTypeEnum soldierSubTypeEnum, BigDecimal subtractNumber) {
        this.totalAttackPointForKillInfantrymanSubTypes.put(soldierSubTypeEnum, (this.totalAttackPointForKillInfantrymanSubTypes.get(soldierSubTypeEnum).subtract(subtractNumber)));
        this.totalAttackPointForKillInfantryman = this.totalAttackPointForKillInfantryman.subtract(subtractNumber);
        return this;
    }

    public TotalAttackPointForKillSoldierMainType subtractRifleSubtype(SoldierSubTypeEnum soldierSubTypeEnum, BigDecimal subtractNumber) {
        this.totalAttackPointForKillRifleSubTypes.put(soldierSubTypeEnum, (this.totalAttackPointForKillRifleSubTypes.get(soldierSubTypeEnum).subtract(subtractNumber)));
        this.totalAttackPointForKillRifle = this.totalAttackPointForKillRifle.subtract(subtractNumber);
        return this;
    }

    public TotalAttackPointForKillSoldierMainType subtractCannonSubtype(SoldierSubTypeEnum soldierSubTypeEnum, BigDecimal subtractNumber) {
        this.totalAttackPointForKillCannonSubTypes.put(soldierSubTypeEnum, (this.totalAttackPointForKillCannonSubTypes.get(soldierSubTypeEnum).subtract(subtractNumber)));
        this.totalAttackPointForKillCannon = this.totalAttackPointForKillCannon.subtract(subtractNumber);
        return this;
    }

    public TotalAttackPointForKillSoldierMainType subtractShipSubtype(SoldierSubTypeEnum soldierSubTypeEnum, BigDecimal subtractNumber) {
        this.totalAttackPointForKillShipSubTypes.put(soldierSubTypeEnum, (this.totalAttackPointForKillShipSubTypes.get(soldierSubTypeEnum).subtract(subtractNumber)));
        this.totalAttackPointForKillShip = this.totalAttackPointForKillShip.subtract(subtractNumber);
        return this;
    }

    public TotalAttackPointForKillSoldierMainType addInfantryManSubtype(SoldierSubTypeEnum soldierSubTypeEnum, BigDecimal addNumber) {
        this.totalAttackPointForKillInfantrymanSubTypes.put(soldierSubTypeEnum, (this.totalAttackPointForKillInfantrymanSubTypes.get(soldierSubTypeEnum).add(addNumber)));
        this.totalAttackPointForKillInfantryman = this.totalAttackPointForKillInfantryman.add(addNumber);
        return this;
    }

    public TotalAttackPointForKillSoldierMainType addRifleSubtype(SoldierSubTypeEnum soldierSubTypeEnum, BigDecimal addNumber) {
        this.totalAttackPointForKillRifleSubTypes.put(soldierSubTypeEnum, (this.totalAttackPointForKillRifleSubTypes.get(soldierSubTypeEnum).add(addNumber)));
        this.totalAttackPointForKillRifle = this.totalAttackPointForKillRifle.add(addNumber);
        return this;
    }

    public TotalAttackPointForKillSoldierMainType addCannonSubtype(SoldierSubTypeEnum soldierSubTypeEnum, BigDecimal addNumber) {
        this.totalAttackPointForKillCannonSubTypes.put(soldierSubTypeEnum, (this.totalAttackPointForKillCannonSubTypes.get(soldierSubTypeEnum).add(addNumber)));
        this.totalAttackPointForKillCannon = this.totalAttackPointForKillCannon.add(addNumber);
        return this;
    }

    public TotalAttackPointForKillSoldierMainType addShipSubtype(SoldierSubTypeEnum soldierSubTypeEnum, BigDecimal addNumber) {
        this.totalAttackPointForKillShipSubTypes.put(soldierSubTypeEnum, (this.totalAttackPointForKillShipSubTypes.get(soldierSubTypeEnum).add(addNumber)));
        this.totalAttackPointForKillShip = this.totalAttackPointForKillShip.add(addNumber);
        return this;
    }

    public BigDecimal calculateAll() {
        return totalAttackPointForKillInfantryman.add(totalAttackPointForKillRifle).add(totalAttackPointForKillCannon).add(totalAttackPointForKillShip);
    }

    /*
    public BigDecimal subtractTotalAttackPointForKillInfantryman(BigDecimal subAttackPoint) {
        return totalAttackPointForKillInfantryman.subtract(subAttackPoint);
    }

    public BigDecimal subtractTotalAttackPointForKillRifle(BigDecimal subAttackPoint) {
        return totalAttackPointForKillRifle.subtract(subAttackPoint);
    }

    public BigDecimal subtractTotalAttackPointForKillCannon(BigDecimal subAttackPoint) {
        return totalAttackPointForKillCannon.subtract(subAttackPoint);
    }

    public BigDecimal subtractTotalAttackPointForKillShip(BigDecimal subAttackPoint) {
        return totalAttackPointForKillShip.subtract(subAttackPoint);
    }*/
}
