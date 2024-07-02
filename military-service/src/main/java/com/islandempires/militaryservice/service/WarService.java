package com.islandempires.militaryservice.service;


import com.islandempires.militaryservice.dto.*;
import com.islandempires.militaryservice.dto.request.WarMilitaryUnitRequest;
import com.islandempires.militaryservice.enums.MissionStatusEnum;
import com.islandempires.militaryservice.enums.MissionTypeEnum;
import com.islandempires.militaryservice.enums.SoldierSubTypeEnum;
import com.islandempires.militaryservice.model.GameServerSoldier;
import com.islandempires.militaryservice.model.IslandMilitary;
import com.islandempires.militaryservice.model.MilitaryUnits;
import com.islandempires.militaryservice.model.production.SoldierProduction;
import com.islandempires.militaryservice.model.resource.RawMaterialsAndPopulationCost;
import com.islandempires.militaryservice.model.soldier.Soldier;
import com.islandempires.militaryservice.model.troopsAction.MovingTroops;
import com.islandempires.militaryservice.model.war.AttackWarReport;
import com.islandempires.militaryservice.rabbitmq.RabbitmqService;
import com.islandempires.militaryservice.repository.*;
import com.islandempires.militaryservice.rabbitmq.RabbitMqConfig;
import com.islandempires.militaryservice.service.client.MilitaryGatewayClient;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class WarService {

    private final IslandMilitaryRepository islandMilitaryRepository;

    private final MovingTroopsRepository movingTroopsRepository;

    private final StationaryTroopsRepository stationaryTroopsRepository;

    private final GameServerSoldierBaseInfoRepository gameServerSoldierBaseInfoRepository;

    private final ModelMapper modelMapper;

    private final WarReportService warReportService;

    private final RabbitmqService rabbitmqService;

    //private final GameServerPropertiesService gameServerPropertiesService;

    private final MilitaryGatewayClient militaryGatewayClient;


    @Transactional
    public IslandMilitary initializeIslandMilitary(String serverId, String islandId, Long userId) {
        if(islandMilitaryRepository.findById(islandId).isPresent()) {
            throw new RuntimeException("already exist");
        }
        GameServerSoldier gameServerSoldier = gameServerSoldierBaseInfoRepository.findById(serverId).orElseThrow();
        IslandMilitary islandMilitary = new IslandMilitary();
        islandMilitary.setIslandId(islandId);
        islandMilitary.setDefensePointChangePercent(1);
        islandMilitary.setUserId(userId);
        islandMilitary.initialize(gameServerSoldier, islandMilitary);
        return islandMilitaryRepository.save(islandMilitary);
    }

    @Transactional
    public IslandMilitary getIslandMilitary(String islandId) {
        return islandMilitaryRepository.findById(islandId).orElseThrow();
    }

    @Transactional
    public void delete(String islandId) {
        islandMilitaryRepository.deleteById(islandId);
    }

    @Transactional
    public IslandMilitary getIslandMilitary(String islandId, Long userId) {
        IslandMilitary islandMilitary = islandMilitaryRepository.findById(islandId).orElseThrow();
        if(!islandMilitary.getUserId().equals(userId)) {
            throw new RuntimeException();
        }
        return islandMilitary;
    }

    public void pullBackTroop(Long troopId, Long userId) {
        MovingTroops movingTroop = movingTroopsRepository.findById(troopId).orElseThrow();
        if(!movingTroop.getOwnerIslandMilitary().getUserId().equals(userId)) {
            throw new RuntimeException();
        }
        movingTroop.setMissionStatus(MissionStatusEnum.RETURNING);
        movingTroop.setStartTime(LocalDateTime.now());
        movingTroopsRepository.save(movingTroop);
        rabbitmqService.sendWarReturningEndMessage(movingTroop.getId(), movingTroop.getDuration().toMillis());
    }

    public void war(String senderIslandMilitaryId, String targetIslandMilitaryId, WarMilitaryUnitRequest warMilitaryUnitRequest, Long userId) {
        IslandMilitary islandMilitary = islandMilitaryRepository.findById(senderIslandMilitaryId).orElseThrow();
        if(userId != null && islandMilitary.getUserId() != userId && islandMilitary.isSoldierCapacitySufficient(warMilitaryUnitRequest)) {
            throw new RuntimeException();
        }

        GameServerSoldier gameServerSoldier = gameServerSoldierBaseInfoRepository.findById("s").orElseThrow();
        MilitaryUnits militaryUnits = new MilitaryUnits();
        militaryUnits.initialize(gameServerSoldier);
        militaryUnits.setOwner(islandMilitary);
        militaryUnits.initializeWithInitialValues(warMilitaryUnitRequest);

        if(!militaryUnits.isShipCapacitySufficient()) {
            throw new RuntimeException();
        }

        sendTroopToTargetIsland(senderIslandMilitaryId, militaryUnits, targetIslandMilitaryId, warMilitaryUnitRequest.getMissionType());
    }

    public void sendTroopToTargetIsland(String senderIslandId, MilitaryUnits militaryUnits, String targetIslandId, MissionTypeEnum missionType) {
        IslandMilitary senderIslandMilitary = islandMilitaryRepository.findById(senderIslandId).orElseThrow();
        IslandMilitary targetIslandMilitary = islandMilitaryRepository.findById(targetIslandId).orElseThrow();

        senderIslandMilitary.getStationaryTroops().getMilitaryUnits().diminishingMilitaryUnitsCount(militaryUnits);

        Double distanceBetweenIslands;
        try {
            distanceBetweenIslands = militaryGatewayClient.getDistanceBetweenIslands(senderIslandId, targetIslandId);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        MovingTroops sendingTroop = new MovingTroops();
        sendingTroop.setOwnerIslandMilitary(senderIslandMilitary);
        sendingTroop.setTargetToIslandMilitary(targetIslandMilitary);
        sendingTroop.setMissionStatus(MissionStatusEnum.GOING);
        sendingTroop.setMissionType(missionType);
        sendingTroop.setMilitaryUnits(militaryUnits);
        sendingTroop.setStartTime(LocalDateTime.now());
        MovingTroops movingTroops = movingTroopsRepository.save(sendingTroop);

        movingTroops.calculateDuration(distanceBetweenIslands);
        MovingTroops calculatedMilitaryTroops = movingTroopsRepository.save(sendingTroop);
        if(missionType.equals(MissionTypeEnum.ATTACK)) {
            rabbitmqService.sendWarAttackEndMessage(calculatedMilitaryTroops.getId(), Duration.ofSeconds(30).toMillis());
        } else if (missionType.equals(MissionTypeEnum.SUPPORT)) {
            rabbitmqService.sendWarSupportEndMessage(calculatedMilitaryTroops.getId(), Duration.ofSeconds(30).toMillis());
        } else {
            throw new RuntimeException();
        }
        islandMilitaryRepository.save(senderIslandMilitary);
    }

    @Scheduled(fixedRateString = "5000")
    public void run() {
        //evaluateBattleVictory("5");
    }

    @Transactional
    @RabbitListener(queues = RabbitMqConfig.MOVING_TROOPS_ATTACK_QUEUE_NAME)
    public void evaluateBattleVictory(String troopIdString) {
        Long troopId = null;
        try {
            troopId = Long.parseLong(troopIdString);
        } catch (NumberFormatException e) {
            return;
        }

        Optional<MovingTroops> movingTroopsOptional = movingTroopsRepository.findById(troopId);

        if(movingTroopsOptional.isEmpty()) {
            return;
        }

        MovingTroops movingTroops = movingTroopsOptional.get();

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
            handleDefenceWin(movingTroops, attackWarReport, totalAttackPoint, totalDefencePoint, gameServerSoldier);
        } else {
            // Attack win
            handleAttackWin(movingTroops, attackWarReport, totalAttackPoint, totalDefencePoint, gameServerSoldier);
        }
    }

    public void handleDefenceWin(MovingTroops movingTroops, AttackWarReport attackWarReport, BigInteger totalAttackPoint, BigInteger totalDefencePoint, GameServerSoldier gameServerSoldier) {
        TotalAttackPointForKillSoldierMainType totalAttackPointForKillSoldierMainType = movingTroops.getMilitaryUnits().calculateTotalAttackPointPerEachOfMainSoldierType();
        SoldierRatios defenderSoldierRatios = movingTroops.getTargetToIslandMilitary().calculateTotalRatioOnIsland();

        BigDecimal strengthDifferenceRatio = new BigDecimal(totalDefencePoint).divide(new BigDecimal(totalAttackPoint), 10, RoundingMode.HALF_UP);
        totalAttackPointForKillSoldierMainType.divideAll(strengthDifferenceRatio);

        List<MilitaryUnitsKilledMilitaryUnitCountDTO> militaryUnitsKilledMilitaryUnitCountDTOList =
                movingTroops.getTargetToIslandMilitary().killSoldiersWithStrengthDifferencePoint(totalAttackPointForKillSoldierMainType, gameServerSoldier, defenderSoldierRatios);

        List<MilitaryUnitsKilledMilitaryUnitCountDTO> militaryUnitsKilledMilitaryUnitCountDTOListClone =
                militaryUnitsKilledMilitaryUnitCountDTOList.stream().map(militaryUnitsKilledMilitaryUnitCountDTO -> {
                    MilitaryUnitsKilledMilitaryUnitCountDTO militaryUnitsKilledMilitaryUnitCountDTOClone = new MilitaryUnitsKilledMilitaryUnitCountDTO();
                    militaryUnitsKilledMilitaryUnitCountDTOClone = militaryUnitsKilledMilitaryUnitCountDTO.cloneMilitaryUnit(militaryUnitsKilledMilitaryUnitCountDTOClone);
                    return militaryUnitsKilledMilitaryUnitCountDTOClone;
                }).collect(Collectors.toList());

        movingTroops.getMilitaryUnits().killAllSoldiers();
        warReportService.prepareAndSaveAttackWarReportAfterWarDefenceWin(attackWarReport, militaryUnitsKilledMilitaryUnitCountDTOListClone);

        movingTroops.getTargetToIslandMilitary().getOtherIslandsIncomingOrDeployedTroops().forEach(troop -> {
            if(troop.getMissionType().equals(MissionTypeEnum.SUPPORT) &&
                troop.getMissionStatus().equals(MissionStatusEnum.DEPLOYED) &&
                troop.getMilitaryUnits().calculateTotalSoldierCount().getInfantrymanCount().compareTo(BigInteger.ZERO) == 0 &&
                troop.getMilitaryUnits().calculateTotalSoldierCount().getRifleCount().compareTo(BigInteger.ZERO) == 0 &&
                troop.getMilitaryUnits().calculateTotalSoldierCount().getCannonCount().compareTo(BigInteger.ZERO) == 0 &&
                troop.getMilitaryUnits().calculateTotalSoldierCount().getShipCount().compareTo(BigInteger.ZERO) == 0 ) {
                troop.setMilitaryUnits(null);
                troop.setIsActive(false);
            }
        });
        movingTroops.setIsActive(false);
        movingTroops.setMilitaryUnits(null);
        movingTroopsRepository.save(movingTroops);
    }

    public void handleAttackWin(MovingTroops movingTroops, AttackWarReport attackWarReport, BigInteger totalAttackPoint, BigInteger totalDefencePoint, GameServerSoldier gameServerSoldier) {
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


        warReportService.prepareAndSaveAttackWarReportAfterWarAttackWin(attackWarReport, militaryUnitsKilledMilitaryUnitCountDTO.getMilitaryUnits());

        movingTroops.getTargetToIslandMilitary().getOtherIslandsIncomingOrDeployedTroops().forEach(troop -> {
            if(troop.getMissionType().equals(MissionTypeEnum.SUPPORT) &&
                    troop.getMissionStatus().equals(MissionStatusEnum.DEPLOYED)) {
                troop.setMilitaryUnits(null);
                troop.setIsActive(false);
            }
        });

        movingTroopsRepository.save(movingTroops);
        rabbitmqService.sendWarReturningEndMessage(movingTroops.getId(), Duration.ofSeconds(30).toMillis());
    }

    @RabbitListener(queues = RabbitMqConfig.MOVING_TROOPS_SUPPORT_QUEUE_NAME)
    public void supportingTroop(String troopIdString) {
        Long troopId = null;
        try {
            troopId = Long.parseLong(troopIdString);
        } catch (NumberFormatException e) {
            return;
        }

        Optional<MovingTroops> movingTroopsOptional = movingTroopsRepository.findById(troopId);
        if(movingTroopsOptional.isEmpty()) {
            return;
        }
        MovingTroops movingTroops = movingTroopsOptional.get();

        movingTroops.setMissionStatus(MissionStatusEnum.DEPLOYED);
        movingTroopsRepository.save(movingTroops);
    }

    @RabbitListener(queues = RabbitMqConfig.MOVING_TROOPS_RETURNING_QUEUE_NAME)
    public void returningTroop(String troopIdString) {
        Long troopId = null;
        try {
            troopId = Long.parseLong(troopIdString);
        } catch (NumberFormatException e) {
            return;
        }

        Optional<MovingTroops> movingTroopsOptional = movingTroopsRepository.findById(troopId);
        if(movingTroopsOptional.isEmpty()) {
            return;
        }
        MovingTroops movingTroops = movingTroopsOptional.get();

        IslandMilitary targetIslandMilitary = movingTroops.getTargetToIslandMilitary();
        targetIslandMilitary.addReturningSoldiers(movingTroops.getMilitaryUnits());

        islandMilitaryRepository.save(targetIslandMilitary);
        movingTroops.setIsActive(false);
        movingTroops.setMilitaryUnits(null);
        movingTroopsRepository.save(movingTroops);
    }


}
