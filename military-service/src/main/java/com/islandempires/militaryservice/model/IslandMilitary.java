package com.islandempires.militaryservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.islandempires.militaryservice.converter.SoldierTypeResearchedConverter;
import com.islandempires.militaryservice.dto.*;
import com.islandempires.militaryservice.dto.request.WarMilitaryUnitRequest;
import com.islandempires.militaryservice.enums.MissionStatusEnum;
import com.islandempires.militaryservice.enums.SoldierSubTypeEnum;
import com.islandempires.militaryservice.enums.SoldierTypeEnum;
import com.islandempires.militaryservice.model.production.SoldierProduction;
import com.islandempires.militaryservice.model.troopsAction.MovingTroops;
import com.islandempires.militaryservice.model.troopsAction.StationaryTroops;
import com.islandempires.militaryservice.model.troopsAction.Troops;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Entity(name = "IslandMilitary")
@AllArgsConstructor
@NoArgsConstructor
public class IslandMilitary {

    @Id
    private String islandId;

    private Long userId;

    private String serverId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "stationaryTroopId", referencedColumnName = "id")
    private StationaryTroops stationaryTroops;

    @OneToMany(mappedBy = "ownerIslandMilitary", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<MovingTroops> myTroops;

    @OneToMany(mappedBy = "targetToIslandMilitary", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<MovingTroops> otherIslandsIncomingOrDeployedTroops;

    @OneToMany(mappedBy = "islandMilitary", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<SoldierProduction> soldierProductionList;

    private int timeReductionPercentageForInfantryman = 100;
    private int timeReductionPercentageForRifle = 100;
    private int timeReductionPercentageForCannon = 100;
    private int timeReductionPercentageForShip = 100;

    private double defenceAndAttackMultiplier = 1;

    private double defencePointPercentage = 0;

    private int visibilityRange = 0;
    private int observableCapacity = 0;

    private int maxMissionaryCount = 0;
    private int totalCapturedIsles = 0;

    @Convert(converter = SoldierTypeResearchedConverter.class)
    private Map<SoldierSubTypeEnum, Boolean> soldierTypeResearched = new HashMap<>();

    public void initialize(GameServerSoldier gameServerSoldier, IslandMilitary islandMilitary) {
        this.stationaryTroops = new StationaryTroops();
        this.stationaryTroops.initialize(gameServerSoldier, islandMilitary);

        this.myTroops = new ArrayList<>();
        this.otherIslandsIncomingOrDeployedTroops = new ArrayList<>();

        initializeSoldierTypeResearched();
    }

    public void initializeSoldierTypeResearched() {
        soldierTypeResearched.put(SoldierSubTypeEnum.PIKEMAN, true);
        soldierTypeResearched.put(SoldierSubTypeEnum.AXEMAN, false);
        soldierTypeResearched.put(SoldierSubTypeEnum.SWORDSMAN, false);
        soldierTypeResearched.put(SoldierSubTypeEnum.ARCHER, false);
        soldierTypeResearched.put(SoldierSubTypeEnum.LIGHT_ARMED_MUSKETEER, false);
        soldierTypeResearched.put(SoldierSubTypeEnum.MEDIUM_ARMED_MUSKETEER, false);
        soldierTypeResearched.put(SoldierSubTypeEnum.HEAVY_ARMED_MUSKETEER, false);
        soldierTypeResearched.put(SoldierSubTypeEnum.CULVERIN, false);
        soldierTypeResearched.put(SoldierSubTypeEnum.MORTAR, false);
        soldierTypeResearched.put(SoldierSubTypeEnum.RIBAULT, false);
        soldierTypeResearched.put(SoldierSubTypeEnum.HOLK, false);
        soldierTypeResearched.put(SoldierSubTypeEnum.GUN_HOLK, false);
        soldierTypeResearched.put(SoldierSubTypeEnum.CARRACK, false);
    }

    public void setSoldierTypeResearched(SoldierSubTypeEnum soldierSubTypeEnum, boolean researched) {
        soldierTypeResearched.put(soldierSubTypeEnum, researched);
    }

    public void addSoldierProduction(SoldierProduction soldierProduction) {
        this.soldierProductionList.add(soldierProduction);
    }

    public void removeSoldierProduction(Long soldierProductionId) {
        this.soldierProductionList.removeIf(soldierProduction -> soldierProduction.getId().equals(soldierProductionId));
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
                troopsOnIsland
                        .stream()
                        .map(troop -> troop.calculateTotalDefencePointOfAllUnitsPerEachSoldierType(defenceAndAttackMultiplier))
                        .reduce(new SoldierTotalDefenceAgainstSoldierType(), SoldierTotalDefenceAgainstSoldierType::addPoints);

        List<MilitaryUnitsKilledMilitaryUnitCountDTO> militaryUnitsKilledMilitaryUnitCountDTOList = new ArrayList<>();
        troopsOnIsland.forEach(troop -> {
            MilitaryUnitsKilledMilitaryUnitCountDTO militaryUnitsKilledMilitaryUnitCountDTO = troop.killSoldiersWithTotalStrengthDifferencePointDefenceWin(
                    totalAttackPointForKillSoldierMainType.multiplyAll(troop.calculateTotalDefencePointOfAllUnitsPerEachSoldierType(defenceAndAttackMultiplier).divideAllValuesWithPerSoldierTypeRatio(totalDefencePointPerEachSoldierType)),
                    troop.calculateTotalDefencePointPerEachSoldierType(defenceAndAttackMultiplier).divideAllValuesWithPerSoldierTypeRatio(totalDefencePointPerEachSoldierType),
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


    public BigDecimal calculateTotalDefencePointOfAllUnits(SoldierRatios soldierRatios) {
        BigDecimal initialDefencePoint = stationaryTroops.getMilitaryUnits().calculateTotalDefencePointOfAllUnits(soldierRatios, defenceAndAttackMultiplier);
        BigDecimal totalDefencePoint = otherIslandsIncomingOrDeployedTroops.stream()
                .filter(troop -> troop.getMissionStatus().equals(MissionStatusEnum.DEPLOYED))
                .map(troop -> troop.getMilitaryUnits().calculateTotalDefencePointOfAllUnits(soldierRatios, defenceAndAttackMultiplier))
                .reduce(initialDefencePoint, BigDecimal::add);
        return totalDefencePoint;
    }

    public Boolean isSoldierCapacitySufficient(WarMilitaryUnitRequest warMilitaryUnitRequest) {
        MilitaryUnits militaryUnit = stationaryTroops.getMilitaryUnits();

        if (
                militaryUnit.getPikeman().getSoldierCount().compareTo(warMilitaryUnitRequest.getPikeman()) >= 0 &&
                        militaryUnit.getAxeman().getSoldierCount().compareTo(warMilitaryUnitRequest.getAxeman()) >= 0 &&
                        militaryUnit.getArchers().getSoldierCount().compareTo(warMilitaryUnitRequest.getArchers()) >= 0 &&
                        militaryUnit.getSwordsman().getSoldierCount().compareTo(warMilitaryUnitRequest.getSwordsman()) >= 0 &&
                        militaryUnit.getLightArmedMusketeer().getSoldierCount().compareTo(warMilitaryUnitRequest.getLightArmedMusketeer()) >= 0 &&
                        militaryUnit.getMediumArmedMusketeer().getSoldierCount().compareTo(warMilitaryUnitRequest.getMediumArmedMusketeer()) >= 0 &&
                        militaryUnit.getHeavyArmedMusketeer().getSoldierCount().compareTo(warMilitaryUnitRequest.getHeavyArmedMusketeer()) >= 0 &&
                        militaryUnit.getCulverin().getSoldierCount().compareTo(warMilitaryUnitRequest.getCulverin()) >= 0 &&
                        militaryUnit.getMortar().getSoldierCount().compareTo(warMilitaryUnitRequest.getMortar()) >= 0 &&
                        militaryUnit.getRibault().getSoldierCount().compareTo(warMilitaryUnitRequest.getRibault()) >= 0 &&
                        militaryUnit.getHolk().getSoldierCount().compareTo(warMilitaryUnitRequest.getHolk()) >= 0 &&
                        militaryUnit.getGunHolk().getSoldierCount().compareTo(warMilitaryUnitRequest.getGunHolk()) >= 0 &&
                        militaryUnit.getCarrack().getSoldierCount().compareTo(warMilitaryUnitRequest.getCarrack()) >= 0
        ) {
            return false;
        }

        return true;
    }

    public void addReturningSoldiers(MilitaryUnits militaryUnit) {
        stationaryTroops.addReturningSoldiers(militaryUnit);
    }

    public int findTimeReductionPercentageOfIsland(SoldierSubTypeEnum soldierSubTypeEnum) {
        SoldierTypeEnum soldierTypeEnum = findSoldierMainTypeWithSubType(soldierSubTypeEnum);
        if(soldierTypeEnum.equals(SoldierTypeEnum.INFANTRYMAN)) {
            return timeReductionPercentageForInfantryman;
        } else if(soldierTypeEnum.equals(SoldierTypeEnum.RIFLE)) {
            return timeReductionPercentageForRifle;
        } else if(soldierTypeEnum.equals(SoldierTypeEnum.CANNON)) {
            return timeReductionPercentageForCannon;
        } else if(soldierTypeEnum.equals(SoldierTypeEnum.SHIP)) {
            return timeReductionPercentageForShip;
        }
        return 100;
    }

    public SoldierTypeEnum findSoldierMainTypeWithSubType(SoldierSubTypeEnum soldierSubTypeEnum) {
        if(soldierSubTypeEnum.equals(SoldierSubTypeEnum.AXEMAN) ||
                soldierSubTypeEnum.equals(SoldierSubTypeEnum.SWORDSMAN) ||
                soldierSubTypeEnum.equals(SoldierSubTypeEnum.ARCHER) ||
                soldierSubTypeEnum.equals(SoldierSubTypeEnum.PIKEMAN) ||
                soldierSubTypeEnum.equals(SoldierSubTypeEnum.MISSIONARY)) {
            return SoldierTypeEnum.INFANTRYMAN;
        } else if(soldierSubTypeEnum.equals(SoldierSubTypeEnum.LIGHT_ARMED_MUSKETEER) ||
                soldierSubTypeEnum.equals(SoldierSubTypeEnum.MEDIUM_ARMED_MUSKETEER) ||
                soldierSubTypeEnum.equals(SoldierSubTypeEnum.HEAVY_ARMED_MUSKETEER)) {
            return SoldierTypeEnum.RIFLE;
        } else if(soldierSubTypeEnum.equals(SoldierSubTypeEnum.MORTAR) ||
                soldierSubTypeEnum.equals(SoldierSubTypeEnum.CULVERIN) ||
                soldierSubTypeEnum.equals(SoldierSubTypeEnum.RIBAULT)) {
            return SoldierTypeEnum.CANNON;
        } else if(soldierSubTypeEnum.equals(SoldierSubTypeEnum.HOLK) ||
                soldierSubTypeEnum.equals(SoldierSubTypeEnum.GUN_HOLK) ||
                soldierSubTypeEnum.equals(SoldierSubTypeEnum.CARRACK)) {
            return SoldierTypeEnum.SHIP;
        }
        return SoldierTypeEnum.INFANTRYMAN;
    }


    @Override
    public String toString() {
        return "IslandMilitary{" +
                "islandId='" + islandId + '\'' +
                ", stationaryTroops=" + stationaryTroops +
                ", myTroops=" + myTroops +
                ", otherIslandsIncomingOrDeployedTroops=" + otherIslandsIncomingOrDeployedTroops +
                '}';
    }
}
