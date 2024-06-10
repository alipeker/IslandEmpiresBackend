package com.islandempires.militaryservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.islandempires.militaryservice.dto.SoldierRatios;
import com.islandempires.militaryservice.dto.SoldierTotalDefenceAgainstSoldierType;
import com.islandempires.militaryservice.dto.TotalSoldierCount;
import com.islandempires.militaryservice.enums.MissionStatusEnum;
import com.islandempires.militaryservice.model.troopsAction.MovingTroops;
import com.islandempires.militaryservice.model.troopsAction.StationaryTroops;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "IslandMilitary")
@AllArgsConstructor
@NoArgsConstructor
public class IslandMilitary {

    @Id
    private String islandId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "stationaryTroopId", referencedColumnName = "id")
    private StationaryTroops stationaryTroops;

    private int defensePointChangePercent;

    @OneToMany(mappedBy = "ownerIslandMilitary", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<MovingTroops> myTroops;

    @OneToMany(mappedBy = "targetToIslandMilitary", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<MovingTroops> otherIslandsIncomingOrDeployedTroops;

    public void initialize(GameServerSoldier gameServerSoldier) {
        this.stationaryTroops = new StationaryTroops();
        this.stationaryTroops.initialize(gameServerSoldier);

        this.myTroops = new ArrayList<>();
        this.otherIslandsIncomingOrDeployedTroops = new ArrayList();
    }


    public SoldierRatios calculateTotalSoldierRatioOnIsland() {
        TotalSoldierCount totalSoldierCount = stationaryTroops.calculateTotalSoldierCount();
        otherIslandsIncomingOrDeployedTroops.stream().filter(Troops -> Troops.getMissionStatus().equals(MissionStatusEnum.DEPLOYED))
                .forEach(troop -> {
                    totalSoldierCount.addSoldierCount(troop.calculateTotalSoldierCount());
                });

        return totalSoldierCount.calculateSoldierRatio();
    }

    public BigInteger calculateTotalDefencePointOfIsland(SoldierRatios soldierRatios) {
        BigDecimal totalDefencePointIslandStationaryTroop = this.stationaryTroops.calculateTotalDefencePointOfAllUnits(soldierRatios);
        otherIslandsIncomingOrDeployedTroops.stream().filter(Troops -> Troops.getMissionStatus().equals(MissionStatusEnum.DEPLOYED))
                .forEach(troop -> {
                    totalDefencePointIslandStationaryTroop.add(troop.calculateTotalDefencePointOfAllUnits(soldierRatios));
                });

        return totalDefencePointIslandStationaryTroop.multiply(BigDecimal.valueOf(defensePointChangePercent)).toBigInteger();
    }

    public BigInteger killSoldiersWithStrengthDifferencePoint(SoldierRatios soldierRatios, BigInteger strengthDifferencePoint) {
        BigDecimal totalDefencePointIslandStationaryTroop = this.stationaryTroops.calculateTotalDefencePointOfAllUnits(soldierRatios);
        BigDecimal totalDefencePointSupportingTroops = BigDecimal.ZERO;
        otherIslandsIncomingOrDeployedTroops.stream().filter(Troops -> Troops.getMissionStatus().equals(MissionStatusEnum.DEPLOYED))
                .forEach(troop -> {
                    totalDefencePointSupportingTroops.add(troop.calculateTotalDefencePointOfAllUnits(soldierRatios));
                });

        double totalKillOfIslandStationaryTroopRatio = totalDefencePointIslandStationaryTroop.divide(totalDefencePointIslandStationaryTroop.add(totalDefencePointSupportingTroops)).doubleValue();
        double totalKillOfSupportingTroopsRatio = totalDefencePointSupportingTroops.divide(totalDefencePointIslandStationaryTroop.add(totalDefencePointSupportingTroops)).doubleValue();
/*
        stationaryTroops.getMilitaryUnits().killSoldiersWithTotalStrengthDifferencePoint(strengthDifferencePoint, totalKillOfIslandStationaryTroopRatio, soldierRatios);
        otherIslandsIncomingOrDeployedTroops.stream().filter(Troops -> Troops.getMissionStatus().equals(MissionStatusEnum.DEPLOYED))
                .forEach(troop -> {
                    troop.getMilitaryUnits().killSoldiersWithTotalStrengthDifferencePoint(strengthDifferencePoint, totalKillOfSupportingTroopsRatio, soldierRatios);
                });*/

        return totalDefencePointIslandStationaryTroop.multiply(BigDecimal.valueOf(defensePointChangePercent)).toBigInteger();
    }

    public void killAllSoldiers() {
        stationaryTroops.getMilitaryUnits().killAllSoldiers();
        otherIslandsIncomingOrDeployedTroops.stream().filter(Troops -> Troops.getMissionStatus().equals(MissionStatusEnum.DEPLOYED))
                .forEach(troop -> {
                    troop.getMilitaryUnits().killAllSoldiers();
                });
    }

    public BigInteger getTotalSoldierCount() {
        BigInteger totalSoldier = stationaryTroops.getMilitaryUnits().getTotalSoldiers();
        otherIslandsIncomingOrDeployedTroops.stream().filter(Troops -> Troops.getMissionStatus().equals(MissionStatusEnum.DEPLOYED))
                .forEach(troop -> {
                    totalSoldier.add(troop.getMilitaryUnits().getTotalSoldiers());
                });
        return totalSoldier;
    }

    public SoldierTotalDefenceAgainstSoldierType calculateTotalDefencePointPerEachSoldierType() {
        SoldierTotalDefenceAgainstSoldierType soldierTotalDefenceAgainstSoldierType = stationaryTroops.getMilitaryUnits().calculateTotalDefencePointPerEachSoldierType();
        otherIslandsIncomingOrDeployedTroops.stream().filter(Troops -> Troops.getMissionStatus().equals(MissionStatusEnum.DEPLOYED))
                .forEach(troop -> {
                    soldierTotalDefenceAgainstSoldierType.addPoints(troop.getMilitaryUnits().calculateTotalDefencePointPerEachSoldierType());
                });
        return soldierTotalDefenceAgainstSoldierType;
    }


    @Override
    public String toString() {
        return "IslandMilitary{" +
                "islandId='" + islandId + '\'' +
                ", stationaryTroops=" + stationaryTroops +
                ", defensePointChangePercent=" + defensePointChangePercent +
                ", myTroops=" + myTroops +
                ", otherIslandsIncomingOrDeployedTroops=" + otherIslandsIncomingOrDeployedTroops +
                '}';
    }
}
