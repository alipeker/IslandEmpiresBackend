package com.islandempires.militaryservice.service;

import com.islandempires.militaryservice.dto.SoldierTotalDefenceAgainstSoldierType;
import com.islandempires.militaryservice.dto.SoldierRatios;
import com.islandempires.militaryservice.enums.MissionStatusEnum;
import com.islandempires.militaryservice.enums.MissionTypeEnum;
import com.islandempires.militaryservice.model.GameServerSoldier;
import com.islandempires.militaryservice.model.IslandMilitary;
import com.islandempires.militaryservice.model.MilitaryUnits;
import com.islandempires.militaryservice.model.troopsAction.MovingTroops;
import com.islandempires.militaryservice.repository.GameServerSoldierBaseInfoRepository;
import com.islandempires.militaryservice.repository.IslandMilitaryRepository;
import com.islandempires.militaryservice.repository.MovingTroopsRepository;
import com.islandempires.militaryservice.repository.StationaryTroopsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class WarService {

    private final IslandMilitaryRepository islandMilitaryRepository;

    private final MovingTroopsRepository movingTroopsRepository;

    private final StationaryTroopsRepository stationaryTroopsRepository;

    private final GameServerSoldierBaseInfoRepository gameServerSoldierBaseInfoRepository;

    private final ModelMapper modelMapper;

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
        sendTroopToTargetIsland("warAttack", militaryUnits, "warDefence", MissionTypeEnum.ATTACK);
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
        SoldierRatios defenderSoldierRatio = movingTroops.getTargetToIslandMilitary().calculateTotalSoldierRatioOnIsland();
        BigInteger totalDefencePoint = movingTroops.getTargetToIslandMilitary().getStationaryTroops().calculateTotalDefencePointOfAllUnits(senderSoldierRatio).toBigInteger();
        BigInteger totalAttackPoint = movingTroops.calculateTotalAttackPointOfAllUnits();

        totalDefencePoint = totalDefencePoint.compareTo(BigInteger.ZERO) == 0 ? BigInteger.ONE : totalDefencePoint;
        totalAttackPoint = totalAttackPoint.compareTo(BigInteger.ZERO) == 0 ? BigInteger.ONE : totalAttackPoint;

        GameServerSoldier gameServerSoldier = movingTroops.getMilitaryUnits().getSwordsman().getSoldierBaseInfo().getGameServerSoldier();

        if (totalDefencePoint.subtract(totalAttackPoint).compareTo(BigInteger.valueOf(0)) > 0) {
            // Defence win
            movingTroops.calculateTotalAttackPointOfAllUnits();
            movingTroops.getTargetToIslandMilitary().killSoldiersWithStrengthDifferencePoint(senderSoldierRatio, totalDefencePoint.subtract(totalAttackPoint).abs());
            movingTroops.getMilitaryUnits().killAllSoldiers();

        } else {
            // Attack win
            SoldierTotalDefenceAgainstSoldierType soldierTotalDefenceAgainstSoldierType = movingTroops.getTargetToIslandMilitary().calculateTotalDefencePointPerEachSoldierType();
            BigDecimal strengthDifferenceRatio = new BigDecimal(totalAttackPoint).divide(new BigDecimal(totalDefencePoint), 10, RoundingMode.HALF_UP);
            movingTroops.getTargetToIslandMilitary().killAllSoldiers();

            movingTroops.getMilitaryUnits()
                            .killSoldiersWithTotalStrengthDifferencePoint(new BigDecimal(totalDefencePoint).divide(strengthDifferenceRatio,10, RoundingMode.HALF_UP),
                                                                  1.0,
                                                                          soldierTotalDefenceAgainstSoldierType,
                                                                          gameServerSoldier);
        }
        movingTroopsRepository.save(movingTroops);
    }

    public void simulateBattleVictory(MilitaryUnits attackerMilitaryUnits, MilitaryUnits targetMilitaryUnits) {
        SoldierRatios attackerSoldierRatio = attackerMilitaryUnits.calculateRatioPerEachSoldierType();
        BigInteger totalAttackPoint = attackerMilitaryUnits.calculateTotalAttackPointOfAllUnits();
        BigInteger totalDefencePoint = targetMilitaryUnits.calculateTotalDefencePointOfAllUnits(attackerSoldierRatio).toBigInteger();
    }

}