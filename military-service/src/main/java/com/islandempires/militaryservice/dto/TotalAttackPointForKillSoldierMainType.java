package com.islandempires.militaryservice.dto;

import com.islandempires.militaryservice.enums.SoldierSubTypeEnum;
import com.islandempires.militaryservice.model.soldier.Soldier;
import com.islandempires.militaryservice.model.soldier.infantryman.Archer;
import com.islandempires.militaryservice.model.soldier.infantryman.Axeman;
import com.islandempires.militaryservice.model.soldier.infantryman.Pikeman;
import com.islandempires.militaryservice.model.soldier.infantryman.Swordsman;
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
@NoArgsConstructor
public class TotalAttackPointForKillSoldierMainType {

    private BigDecimal totalAttackPointForKillInfantryman;

    private BigDecimal totalAttackPointForKillRifle;

    private BigDecimal totalAttackPointForKillCannon ;

    private BigDecimal totalAttackPointForKillShip;

    private HashMap<SoldierSubTypeEnum, BigDecimal> totalAttackPointForKillInfantrymanSubTypes = new HashMap();

    private HashMap<SoldierSubTypeEnum, BigDecimal> totalAttackPointForKillRifleSubTypes = new HashMap();

    private HashMap<SoldierSubTypeEnum, BigDecimal> totalAttackPointForKillCannonSubTypes = new HashMap();

    private HashMap<SoldierSubTypeEnum, BigDecimal> totalAttackPointForKillShipSubTypes = new HashMap();

    public void calculateTotalAttackPointForKillInfantrymanSubTypes(List<Soldier> soldierAttackPoints) {
        BigInteger sum = soldierAttackPoints.stream()
                .map(Soldier::calculateTotalAttackPoint)
                .reduce(BigInteger.ZERO, BigInteger::add);

        soldierAttackPoints.stream().forEach(soldier -> {
                BigDecimal soldierTotalAttackPoint = new BigDecimal(soldier.calculateTotalAttackPoint()).divide(new BigDecimal(sum), 10, RoundingMode.HALF_UP).multiply(totalAttackPointForKillInfantryman);
                totalAttackPointForKillInfantrymanSubTypes.put(SoldierSubTypeEnum.valueOf(soldier.getSoldierBaseInfo().getSoldierSubTypeName()), soldierTotalAttackPoint);
        });
    }

    public void calculateTotalAttackPointForKillRifleSubTypes(List<Soldier> soldierAttackPoints) {
        BigInteger sum = soldierAttackPoints.stream()
                .map(Soldier::calculateTotalAttackPoint)
                .reduce(BigInteger.ZERO, BigInteger::add);

        soldierAttackPoints.stream().forEach(soldier -> {
            BigDecimal soldierTotalAttackPoint = new BigDecimal(soldier.calculateTotalAttackPoint()).divide(new BigDecimal(sum), 10, RoundingMode.HALF_UP).multiply(totalAttackPointForKillRifle);
            totalAttackPointForKillRifleSubTypes.put(SoldierSubTypeEnum.valueOf(soldier.getSoldierBaseInfo().getSoldierSubTypeName()), soldierTotalAttackPoint);
        });
    }

    public void calculateTotalAttackPointForKillCannonSubTypes(List<Soldier> soldierAttackPoints) {
        BigInteger sum = soldierAttackPoints.stream()
                .map(Soldier::calculateTotalAttackPoint)
                .reduce(BigInteger.ZERO, BigInteger::add);

        soldierAttackPoints.stream().forEach(soldier -> {
            BigDecimal soldierTotalAttackPoint = new BigDecimal(soldier.calculateTotalAttackPoint()).divide(new BigDecimal(sum), 10, RoundingMode.HALF_UP).multiply(totalAttackPointForKillCannon);
            totalAttackPointForKillCannonSubTypes.put(SoldierSubTypeEnum.valueOf(soldier.getSoldierBaseInfo().getSoldierSubTypeName()), soldierTotalAttackPoint);
        });
    }

    public void calculateTotalAttackPointForKillShipSubTypes(List<Soldier> soldierAttackPoints) {
        BigInteger sum = soldierAttackPoints.stream()
                .map(Soldier::calculateTotalAttackPoint)
                .reduce(BigInteger.ZERO, BigInteger::add);

        soldierAttackPoints.stream().forEach(soldier -> {
            BigDecimal soldierTotalAttackPoint = new BigDecimal(soldier.calculateTotalAttackPoint()).divide(new BigDecimal(sum), 10, RoundingMode.HALF_UP).multiply(totalAttackPointForKillShip);
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
