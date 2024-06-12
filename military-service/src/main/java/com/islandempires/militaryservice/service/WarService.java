package com.islandempires.militaryservice.service;

import com.islandempires.militaryservice.dto.MilitaryUnitsKilledMilitaryUnitCountDTO;
import com.islandempires.militaryservice.dto.SoldierTotalDefenceAgainstSoldierType;
import com.islandempires.militaryservice.dto.SoldierRatios;
import com.islandempires.militaryservice.dto.TotalAttackPointForKillSoldierMainType;
import com.islandempires.militaryservice.enums.MissionStatusEnum;
import com.islandempires.militaryservice.enums.MissionTypeEnum;
import com.islandempires.militaryservice.model.GameServerSoldier;
import com.islandempires.militaryservice.model.IslandMilitary;
import com.islandempires.militaryservice.model.MilitaryUnits;
import com.islandempires.militaryservice.model.troopsAction.MovingTroops;
import com.islandempires.militaryservice.model.war.AttackWarReport;
import com.islandempires.militaryservice.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
public class WarService {

    private final IslandMilitaryRepository islandMilitaryRepository;

    private final MovingTroopsRepository movingTroopsRepository;

    private final StationaryTroopsRepository stationaryTroopsRepository;

    private final GameServerSoldierBaseInfoRepository gameServerSoldierBaseInfoRepository;

    private final ModelMapper modelMapper;

    private final WarReportService warReportService;

    private final TroopsRepository troopsRepository;

    @Transactional
    public IslandMilitary initializeIslandMilitary(String serverId, String islandId) {
        if(islandMilitaryRepository.findById(islandId).isPresent()) {
            throw new RuntimeException("already exist");
        }
        GameServerSoldier gameServerSoldier = gameServerSoldierBaseInfoRepository.findById(serverId).orElseThrow();
        IslandMilitary islandMilitary = new IslandMilitary();
        islandMilitary.setIslandId(islandId);
        islandMilitary.setDefensePointChangePercent(1);
        islandMilitary.initialize(gameServerSoldier);
        return islandMilitaryRepository.save(islandMilitary);
    }

    @Transactional
    public IslandMilitary getIslandMilitary(String islandId) {
        return islandMilitaryRepository.findById(islandId).orElseThrow();
    }

    public void test() {
        GameServerSoldier gameServerSoldier = gameServerSoldierBaseInfoRepository.findById("s").orElseThrow();
        MilitaryUnits militaryUnits = new MilitaryUnits();
        militaryUnits.initialize(gameServerSoldier);
        militaryUnits.setOwner(islandMilitaryRepository.findById("warAttack").orElseThrow());
        militaryUnits.getMortar().setSoldierCount(BigInteger.valueOf(20));
        militaryUnits.getHeavyArmedMusketeer().setSoldierCount(BigInteger.valueOf(50));
        sendTroopToTargetIsland("warAttack", militaryUnits, "warDefence", MissionTypeEnum.SUPPORT);
    }

    public void sendTroopToTargetIsland(String senderIslandId, MilitaryUnits militaryUnits, String targetIslandId, MissionTypeEnum missionType) {
        IslandMilitary senderIslandMilitary = islandMilitaryRepository.findById(senderIslandId).orElseThrow();
        IslandMilitary targetIslandMilitary = islandMilitaryRepository.findById(targetIslandId).orElseThrow();

        senderIslandMilitary.getStationaryTroops().getMilitaryUnits().diminishingMilitaryUnitsCount(militaryUnits);

        // get request to island service for get duration between islands
        MovingTroops sendingTroop = new MovingTroops();
        sendingTroop.setOwnerIslandMilitary(senderIslandMilitary);
        sendingTroop.setTargetToIslandMilitary(targetIslandMilitary);
        sendingTroop.setDuration(Duration.ofMinutes(5));
        sendingTroop.setMissionStatus(MissionStatusEnum.GOING);
        sendingTroop.setMissionType(missionType);
        sendingTroop.setMilitaryUnits(militaryUnits);
        sendingTroop.setStartTime(LocalDateTime.now());
        movingTroopsRepository.save(sendingTroop);
    }


    @Transactional
    public void evaluateBattleVictory(Long troopId) {
        MovingTroops movingTroops = movingTroopsRepository.findById(troopId).orElseThrow();
        SoldierRatios senderSoldierRatio = movingTroops.calculateRatioPerEachMainSoldierType();
        if(senderSoldierRatio.isAllValueZero()) {
            senderSoldierRatio = senderSoldierRatio.setAllValueAsOne();
        }
        BigInteger totalDefencePoint = movingTroops.getTargetToIslandMilitary().calculateTotalDefencePointOfAllUnits(senderSoldierRatio).toBigInteger();
        BigInteger totalAttackPoint = movingTroops.calculateTotalAttackPointOfAllUnits();

        totalDefencePoint = totalDefencePoint.compareTo(BigInteger.ZERO) == 0 ? BigInteger.ONE : totalDefencePoint;
        totalAttackPoint = totalAttackPoint.compareTo(BigInteger.ZERO) == 0 ? BigInteger.ONE : totalAttackPoint;


        GameServerSoldier gameServerSoldier = movingTroops.getMilitaryUnits().getSwordsman().getSoldierBaseInfo().getGameServerSoldier();

        AttackWarReport attackWarReport = warReportService.prepareAttackWarReportBeforeWar(movingTroops);

        if (totalDefencePoint.subtract(totalAttackPoint).compareTo(BigInteger.valueOf(0)) > 0) {
            // Defence win
            TotalAttackPointForKillSoldierMainType totalAttackPointForKillSoldierMainType = movingTroops.getMilitaryUnits().calculateTotalAttackPointPerEachOfMainSoldierType();
            SoldierRatios defenderSoldierRatios = movingTroops.getTargetToIslandMilitary().calculateTotalRatioOnIsland();

            BigDecimal strengthDifferenceRatio = new BigDecimal(totalDefencePoint).divide(new BigDecimal(totalAttackPoint), 10, RoundingMode.HALF_UP);
            totalAttackPointForKillSoldierMainType.divideAll(strengthDifferenceRatio);

            List<MilitaryUnitsKilledMilitaryUnitCountDTO> militaryUnitsKilledMilitaryUnitCountDTOList =
                    movingTroops.getTargetToIslandMilitary().killSoldiersWithStrengthDifferencePoint(totalAttackPointForKillSoldierMainType, gameServerSoldier, defenderSoldierRatios);

            movingTroops.getMilitaryUnits().killAllSoldiers();
            warReportService.prepareAndSaveAttackWarReportAfterWarDefenceWin(attackWarReport, movingTroops, militaryUnitsKilledMilitaryUnitCountDTOList);

            IslandMilitary ownerIslandMilitary = movingTroops.getOwnerIslandMilitary();
            IslandMilitary targetIslandMilitary = movingTroops.getOwnerIslandMilitary();

            ownerIslandMilitary.getMyTroops().removeIf(removeTroop -> removeTroop.getId().equals(movingTroops.getId()));
            targetIslandMilitary.getOtherIslandsIncomingOrDeployedTroops().removeIf(removeTroop -> removeTroop.getId().equals(movingTroops.getId()));
            targetIslandMilitary.getOtherIslandsIncomingOrDeployedTroops().removeIf(removeTroop ->
                removeTroop.getMilitaryUnits().calculateTotalSoldierCount().getInfantrymanCount().compareTo(BigInteger.ZERO) == 0 &&
                removeTroop.getMilitaryUnits().calculateTotalSoldierCount().getRifleCount().compareTo(BigInteger.ZERO) == 0 &&
                removeTroop.getMilitaryUnits().calculateTotalSoldierCount().getCannonCount().compareTo(BigInteger.ZERO) == 0 &&
                removeTroop.getMilitaryUnits().calculateTotalSoldierCount().getShipCount().compareTo(BigInteger.ZERO) == 0
            );

            movingTroops.setMilitaryUnits(null);
            MovingTroops movingTroop = movingTroopsRepository.save(movingTroops);

            movingTroopsRepository.deleteByIdQuery(movingTroop.getId());
        } else {
            // Attack win
            BigDecimal strengthDifferenceRatio = new BigDecimal(totalAttackPoint).divide(new BigDecimal(totalDefencePoint), 10, RoundingMode.HALF_UP);
            SoldierTotalDefenceAgainstSoldierType soldierTotalDefenceAgainstSoldierType = movingTroops.getTargetToIslandMilitary().getStationaryTroops().calculateTotalDefencePointOfAllUnitsPerEachSoldierType();
            soldierTotalDefenceAgainstSoldierType = soldierTotalDefenceAgainstSoldierType.divideAllValues(strengthDifferenceRatio);
            movingTroops.getTargetToIslandMilitary().killAllSoldiers();

            MilitaryUnitsKilledMilitaryUnitCountDTO militaryUnitsKilledMilitaryUnitCountDTO = movingTroops.getMilitaryUnits()
                                                                                                .killSoldiersWithTotalStrengthDifferencePoint(soldierTotalDefenceAgainstSoldierType,
                                                                                                                                              BigDecimal.ONE,
                                                                                                                                              gameServerSoldier);
            movingTroops.setStartTime(LocalDateTime.now());
            movingTroops.setMissionStatus(MissionStatusEnum.RETURNING);

            warReportService.prepareAndSaveAttackWarReportAfterWarAttackWin(attackWarReport, movingTroops, militaryUnitsKilledMilitaryUnitCountDTO.getMilitaryUnits());

            movingTroopsRepository.save(movingTroops);

        }
    }

    public void simulateBattleVictory(MilitaryUnits attackerMilitaryUnits, MilitaryUnits targetMilitaryUnits) {
        SoldierRatios attackerSoldierRatio = attackerMilitaryUnits.calculateRatioPerEachSoldierType();
        BigInteger totalAttackPoint = attackerMilitaryUnits.calculateTotalAttackPointOfAllUnits();
        BigInteger totalDefencePoint = targetMilitaryUnits.calculateTotalDefencePointOfAllUnits(attackerSoldierRatio).toBigInteger();
    }

}
