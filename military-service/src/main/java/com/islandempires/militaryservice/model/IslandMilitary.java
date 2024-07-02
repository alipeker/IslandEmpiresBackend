package com.islandempires.militaryservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.islandempires.militaryservice.dto.*;
import com.islandempires.militaryservice.dto.request.WarMilitaryUnitRequest;
import com.islandempires.militaryservice.enums.MissionStatusEnum;
import com.islandempires.militaryservice.enums.SoldierSubTypeEnum;
import com.islandempires.militaryservice.model.production.SoldierProduction;
import com.islandempires.militaryservice.model.troopsAction.MovingTroops;
import com.islandempires.militaryservice.model.troopsAction.StationaryTroops;
import com.islandempires.militaryservice.model.troopsAction.Troops;
import com.islandempires.militaryservice.model.war.WarReport;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity(name = "IslandMilitary")
@AllArgsConstructor
@NoArgsConstructor
public class IslandMilitary {

    @Id
    private String islandId;

    private Long userId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "stationaryTroopId", referencedColumnName = "id")
    private StationaryTroops stationaryTroops;

    private int defensePointChangePercent;

    @OneToMany(mappedBy = "ownerIslandMilitary", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnore
    private List<MovingTroops> myTroops;

    @OneToMany(mappedBy = "targetToIslandMilitary", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnore
    private List<MovingTroops> otherIslandsIncomingOrDeployedTroops;

    @OneToMany(mappedBy = "islandMilitary", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnore
    private List<SoldierProduction> soldierProductionList;

    public void initialize(GameServerSoldier gameServerSoldier, IslandMilitary islandMilitary) {
        this.stationaryTroops = new StationaryTroops();
        this.stationaryTroops.initialize(gameServerSoldier, islandMilitary);

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

    public void addSoldierWithSoldierSubTypeEnum(SoldierSubTypeEnum soldierSubTypeEnum) {
        this.stationaryTroops.getMilitaryUnits().addSoldier(soldierSubTypeEnum, BigInteger.ONE);
    }

    public SoldierRatios calculateTotalSoldierRatioOnIsland() {
        TotalSoldierCount totalSoldierCount = stationaryTroops.calculateTotalSoldierCount();
        otherIslandsIncomingOrDeployedTroops.stream().filter(Troops -> Troops.getMissionStatus().equals(MissionStatusEnum.DEPLOYED))
                .forEach(troop -> {
                    totalSoldierCount.addSoldierCount(troop.calculateTotalSoldierCount());
                });

        return totalSoldierCount.calculateSoldierRatio();
    }

    public List<Troops> getAllTroopsOnIsland() {
        List<Troops> troopOnIslandList = new ArrayList<>();
        troopOnIslandList.add(stationaryTroops);
        troopOnIslandList.addAll(otherIslandsIncomingOrDeployedTroops.stream().filter(Troops -> Troops.getMissionStatus().equals(MissionStatusEnum.DEPLOYED)).collect(Collectors.toList()));
        return troopOnIslandList;
    }

    public List<MilitaryUnitsKilledMilitaryUnitCountDTO> killSoldiersWithStrengthDifferencePoint(TotalAttackPointForKillSoldierMainType totalAttackPointForKillSoldierMainType, GameServerSoldier gameServerSoldier, SoldierRatios defenceSoldierRatios) {
        List<Troops> troopsOnIsland = getAllTroopsOnIsland();
        SoldierTotalDefenceAgainstSoldierType totalDefencePointPerEachSoldierType =
                troopsOnIsland.stream().map(Troops::calculateTotalDefencePointOfAllUnitsPerEachSoldierType)
                        .reduce(new SoldierTotalDefenceAgainstSoldierType(), SoldierTotalDefenceAgainstSoldierType::addPoints);

        List<MilitaryUnitsKilledMilitaryUnitCountDTO> militaryUnitsKilledMilitaryUnitCountDTOList = new ArrayList<>();
        troopsOnIsland.forEach(troop -> {
            MilitaryUnitsKilledMilitaryUnitCountDTO militaryUnitsKilledMilitaryUnitCountDTO = troop.killSoldiersWithTotalStrengthDifferencePointDefenceWin(
                    totalAttackPointForKillSoldierMainType.multiplyAll(troop.calculateTotalDefencePointOfAllUnitsPerEachSoldierType().divideAllValuesWithPerSoldierTypeRatio(totalDefencePointPerEachSoldierType)),
                    troop.calculateTotalDefencePointPerEachSoldierType().divideAllValuesWithPerSoldierTypeRatio(totalDefencePointPerEachSoldierType),
                    gameServerSoldier);
            militaryUnitsKilledMilitaryUnitCountDTO.getMilitaryUnits().setOwner(troop.getMilitaryUnits().getOwner());
            militaryUnitsKilledMilitaryUnitCountDTOList.add(militaryUnitsKilledMilitaryUnitCountDTO);
        });
        return militaryUnitsKilledMilitaryUnitCountDTOList;
    }

    public SoldierRatios calculateTotalRatioOnIsland() {
        SoldierRatios soldierRatios = this.stationaryTroops.getMilitaryUnits().calculateRatioPerEachSoldierType();
        otherIslandsIncomingOrDeployedTroops.stream()
                .filter(troops -> troops.getMissionStatus().equals(MissionStatusEnum.DEPLOYED))
                .map(Troops::getMilitaryUnits)
                .map(MilitaryUnits::calculateRatioPerEachSoldierType)
                .forEach(soldierRatios::addSoldierRatio);
        return soldierRatios;
    }

    public void killAllSoldiers() {
        stationaryTroops.getMilitaryUnits().killAllSoldiers();
        otherIslandsIncomingOrDeployedTroops.removeIf(troop -> troop.getMissionStatus().equals(MissionStatusEnum.DEPLOYED));
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

    public BigDecimal calculateTotalDefencePointOfAllUnits(SoldierRatios soldierRatios) {
        BigDecimal initialDefencePoint = stationaryTroops.getMilitaryUnits().calculateTotalDefencePointOfAllUnits(soldierRatios);
        BigDecimal totalDefencePoint = otherIslandsIncomingOrDeployedTroops.stream()
                .filter(troop -> troop.getMissionStatus().equals(MissionStatusEnum.DEPLOYED))
                .map(troop -> troop.getMilitaryUnits().calculateTotalDefencePointOfAllUnits(soldierRatios))
                .reduce(initialDefencePoint, BigDecimal::add);
        return totalDefencePoint;
    }

    public Boolean isSoldierCapacitySufficient(WarMilitaryUnitRequest warMilitaryUnitRequest) {
        MilitaryUnits militaryUnit = stationaryTroops.getMilitaryUnits();

        if (militaryUnit.getPikeman().getSoldierCount().compareTo(warMilitaryUnitRequest.getPikeman()) >= 0) {
            return false;
        }
        if (militaryUnit.getAxeman().getSoldierCount().compareTo(warMilitaryUnitRequest.getAxeman()) >= 0) {
            return false;
        }
        if (militaryUnit.getArchers().getSoldierCount().compareTo(warMilitaryUnitRequest.getArchers()) >= 0) {
            return false;
        }
        if (militaryUnit.getSwordsman().getSoldierCount().compareTo(warMilitaryUnitRequest.getSwordsman()) >= 0) {
            return false;
        }
        if (militaryUnit.getLightArmedMusketeer().getSoldierCount().compareTo(warMilitaryUnitRequest.getLightArmedMusketeer()) >= 0) {
            return false;
        }
        if (militaryUnit.getMediumArmedMusketeer().getSoldierCount().compareTo(warMilitaryUnitRequest.getMediumArmedMusketeer()) >= 0) {
            return false;
        }
        if (militaryUnit.getHeavyArmedMusketeer().getSoldierCount().compareTo(warMilitaryUnitRequest.getHeavyArmedMusketeer()) >= 0) {
            return false;
        }
        if (militaryUnit.getCulverin().getSoldierCount().compareTo(warMilitaryUnitRequest.getCulverin()) >= 0) {
            return false;
        }
        if (militaryUnit.getMortar().getSoldierCount().compareTo(warMilitaryUnitRequest.getMortar()) >= 0) {
            return false;
        }
        if (militaryUnit.getRibault().getSoldierCount().compareTo(warMilitaryUnitRequest.getRibault()) >= 0) {
            return false;
        }
        if (militaryUnit.getHolk().getSoldierCount().compareTo(warMilitaryUnitRequest.getHolk()) >= 0) {
            return false;
        }
        if (militaryUnit.getGunHolk().getSoldierCount().compareTo(warMilitaryUnitRequest.getGunHolk()) >= 0) {
            return false;
        }
        if (militaryUnit.getCarrack().getSoldierCount().compareTo(warMilitaryUnitRequest.getCarrack()) >= 0) {
            return false;
        }

        return true;
    }

    public void addReturningSoldiers(MilitaryUnits militaryUnit) {
        stationaryTroops.addReturningSoldiers(militaryUnit);
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
