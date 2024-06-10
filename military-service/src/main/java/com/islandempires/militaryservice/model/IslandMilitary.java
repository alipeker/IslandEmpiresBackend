package com.islandempires.militaryservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public List<MilitaryUnits> getAllMilitaryUnitsOnIsland() {
        List<MilitaryUnits> militaryUnits = new ArrayList<>();
        militaryUnits.add(stationaryTroops.getMilitaryUnits());
        otherIslandsIncomingOrDeployedTroops.stream().filter(Troops -> Troops.getMissionStatus().equals(MissionStatusEnum.DEPLOYED))
                .forEach(troop -> {
                    militaryUnits.add(troop.getMilitaryUnits());
                });

        return militaryUnits;
    }

    public SoldierRatios calculateTotalSoldierRatioOnIsland() {
        TotalSoldierCount totalSoldierCount = stationaryTroops.calculateTotalSoldierCount();
        otherIslandsIncomingOrDeployedTroops.stream().filter(Troops -> Troops.getMissionStatus().equals(MissionStatusEnum.DEPLOYED))
                .forEach(troop -> {
                    totalSoldierCount.addSoldierCount(troop.calculateTotalSoldierCount());
                });

        return totalSoldierCount.calculateSoldierRatio();
    }

    public BigDecimal calculateTotalDefencePointOnIsland(SoldierRatios soldierRatios) {
        BigDecimal totalDefencePoint = stationaryTroops.calculateTotalDefencePointOfAllUnits(soldierRatios);
        totalDefencePoint.add(otherIslandsIncomingOrDeployedTroops.stream().filter(Troops -> Troops.getMissionStatus().equals(MissionStatusEnum.DEPLOYED))
                .map(troop -> troop.calculateTotalDefencePointOfAllUnits(soldierRatios))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        return totalDefencePoint;
    }

    public BigInteger killSoldiersWithStrengthDifferencePoint(SoldierRatios soldierRatios, BigDecimal strengthDifferencePoint, GameServerSoldier gameServerSoldier) {
        /*
        BigDecimal totalDefencePointOnIsland = calculateTotalDefencePointOnIsland(soldierRatios);

        BigDecimal ratio = stationaryTroops.calculateTotalDefencePointOfAllUnits(soldierRatios).divide(totalDefencePointOnIsland);
        stationaryTroops.getMilitaryUnits().killSoldiersWithTotalStrengthDifferencePointAttackWin(strengthDifferencePoint,
                totalDefencePointOnIsland,
                gameServerSoldier);
        return totalDefencePointIslandStationaryTroop.multiply(BigDecimal.valueOf(defensePointChangePercent)).toBigInteger();*/
        return null;
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
