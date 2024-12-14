package com.islandempires.militaryservice.service;

import com.islandempires.militaryservice.dto.CreateSoldierResponseDTO;
import com.islandempires.militaryservice.dto.IslandResourceDTO;
import com.islandempires.militaryservice.enums.SoldierSubTypeEnum;
import com.islandempires.militaryservice.enums.SoldierTypeEnum;
import com.islandempires.militaryservice.exception.CustomRunTimeException;
import com.islandempires.militaryservice.exception.ExceptionE;
import com.islandempires.militaryservice.model.IslandMilitary;
import com.islandempires.militaryservice.model.production.SoldierProduction;
import com.islandempires.militaryservice.model.resource.RawMaterialsAndPopulationCost;
import com.islandempires.militaryservice.model.soldier.Soldier;
import com.islandempires.militaryservice.rabbitmq.RabbitMqConfig;
import com.islandempires.militaryservice.rabbitmq.RabbitmqService;
import com.islandempires.militaryservice.repository.IslandMilitaryRepository;
import com.islandempires.militaryservice.repository.SoldierProductionRepository;
import com.islandempires.militaryservice.service.client.MilitaryGatewayClient;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SoldierService {

    private final IslandMilitaryRepository islandMilitaryRepository;


    private final SoldierProductionRepository soldierProductionRepository;

    private final MilitaryGatewayClient militaryGatewayClient;

    private final RabbitmqService rabbitmqService;

    private Map<List<SoldierSubTypeEnum>, SoldierTypeEnum> soldierTypeList = new HashMap<>();

    public SoldierService(IslandMilitaryRepository islandMilitaryRepository, SoldierProductionRepository soldierProductionRepository,
                          MilitaryGatewayClient militaryGatewayClient, RabbitmqService rabbitmqService) {
        this.islandMilitaryRepository = islandMilitaryRepository;
        this.soldierProductionRepository = soldierProductionRepository;
        this.militaryGatewayClient = militaryGatewayClient;
        this.rabbitmqService = rabbitmqService;

        this.soldierTypeList.put(List.of(SoldierSubTypeEnum.PIKEMAN, SoldierSubTypeEnum.AXEMAN, SoldierSubTypeEnum.SWORDSMAN, SoldierSubTypeEnum.ARCHER),
                SoldierTypeEnum.INFANTRYMAN);
        this.soldierTypeList.put(List.of(SoldierSubTypeEnum.LIGHT_ARMED_MUSKETEER, SoldierSubTypeEnum.MEDIUM_ARMED_MUSKETEER, SoldierSubTypeEnum.HEAVY_ARMED_MUSKETEER),
                SoldierTypeEnum.RIFLE);
        this.soldierTypeList.put(List.of(SoldierSubTypeEnum.CULVERIN, SoldierSubTypeEnum.MORTAR, SoldierSubTypeEnum.RIBAULT),
                SoldierTypeEnum.CANNON);
        this.soldierTypeList.put(List.of(SoldierSubTypeEnum.HOLK, SoldierSubTypeEnum.GUN_HOLK, SoldierSubTypeEnum.CARRACK), SoldierTypeEnum.SHIP);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CreateSoldierResponseDTO createSoldier(String islandId, Long userId, SoldierSubTypeEnum soldierSubTypeEnum, int soldierCount) {
        if(soldierCount <= 0) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }
        IslandMilitary islandMilitary = islandMilitaryRepository.findById(islandId).orElseThrow();
        if (!islandMilitary.getUserId().equals(userId)) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }

        Optional<List<SoldierSubTypeEnum>> sameSoldierTypesOptional = this.soldierTypeList
                .keySet()
                .stream()
                .filter(e -> e.stream().anyMatch(se -> se.equals(soldierSubTypeEnum)))
                .findFirst();

        long sameSoldierTypeRecruitCount = islandMilitary.getSoldierProductionList().stream()
                .filter(soldierProduction -> sameSoldierTypesOptional.get().contains(soldierProduction.getSoldierSubType()))
                .count();

        if (sameSoldierTypesOptional.isEmpty() ||
                sameSoldierTypeRecruitCount >= 3) {
            throw new CustomRunTimeException(ExceptionE.RECRUITING_QUEUE_ERROR);
        }

        if (islandMilitary.getSoldierTypeResearched().get(soldierSubTypeEnum)) {
            Soldier soldier = islandMilitary.getStationaryTroops().getMilitaryUnits().getSoldiersWithSoldierSubTypeEnum(soldierSubTypeEnum);

            RawMaterialsAndPopulationCost rawMaterialsAndPopulationCost = null;
            try {
                rawMaterialsAndPopulationCost = (RawMaterialsAndPopulationCost) soldier.getSoldierBaseInfo().getRawMaterialsAndPopulationCost().clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            rawMaterialsAndPopulationCost.setId(null);
            Duration duration = soldier.getSoldierBaseInfo().getProductionDuration();
            duration = multiplyDuration(duration,
                    (double) islandMilitary.findTimeReductionPercentageOfIsland(soldierSubTypeEnum) / 100);

            SoldierProduction soldierProduction = new SoldierProduction();
            soldierProduction.setSoldierCount(soldierCount);
            soldierProduction.setTotalSoldierCount(soldierCount);
            soldierProduction.setSoldierSubType(soldierSubTypeEnum);
            soldierProduction.setProductionDuration(duration);
            soldierProduction.setRawMaterialsAndPopulationCost(rawMaterialsAndPopulationCost);
            soldierProduction.setIslandMilitaryId(islandMilitary.getIslandId());
            soldierProduction.setTime(LocalDateTime.now());
            soldierProduction.setActualStartTimestamp(System.currentTimeMillis());
            soldierProduction.setAllActualStartTimeStamp(System.currentTimeMillis());
            islandMilitary.addSoldierProduction(soldierProduction);
            IslandMilitary recordedIslandMilitary = islandMilitaryRepository.save(islandMilitary);

            SoldierProduction savedSoldierProduction = recordedIslandMilitary.getSoldierProductionList()
                    .stream()
                    .filter(sp -> sp.equals(soldierProduction))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("SoldierProduction not found"));

            if(sameSoldierTypeRecruitCount == 0) {
                rabbitmqService.sendSoldierProductionEndMessage(savedSoldierProduction.getId(), soldierProduction.getProductionDuration().toMillis());
            }

            RawMaterialsAndPopulationCost rawMaterialsAndPopulationCostAll = soldier.getSoldierBaseInfo().getRawMaterialsAndPopulationCost().multiply(soldierCount);
            IslandResourceDTO islandResourceDTO = militaryGatewayClient.assignResources(islandId, rawMaterialsAndPopulationCostAll);

            return new CreateSoldierResponseDTO(islandResourceDTO, recordedIslandMilitary);
        } else {
            throw new RuntimeException();
        }
    }

    public Duration multiplyDuration(Duration duration, double multiplier) {
        long nanoseconds = duration.toNanos();

        double multipliedNanos = nanoseconds * multiplier;

        return Duration.ofNanos((long) multipliedNanos);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public IslandResourceDTO cancelSoldierProduction(Long soldierProductionId, Long userId) {
        SoldierProduction soldierProduction = soldierProductionRepository.findById(soldierProductionId).orElseThrow();

        if (!soldierProduction.getIslandMilitary().getUserId().equals(userId) || soldierProduction.getSoldierCount() <= 1) {
            throw new RuntimeException();
        }

        int soldierCount = soldierProduction.getSoldierCount() - 1;
        soldierProduction.setSoldierCount(1);
        SoldierProduction recordedSoldierProduction = soldierProductionRepository.save(soldierProduction);

        sendToClient(recordedSoldierProduction.getIslandMilitary());

        return militaryGatewayClient.refundResources(soldierProduction.getIslandMilitaryId(), soldierProduction.getRawMaterialsAndPopulationCost().multiply(soldierCount));
    }

    @RabbitListener(queues = RabbitMqConfig.SOLDIER_CRATE_QUEUE_NAME)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void soldierCreated(String soldierProductionIdString) {
        Long soldierProductionId = null;
        try {
            soldierProductionId = Long.parseLong(soldierProductionIdString);
        } catch (NumberFormatException e) {
            return;
        }

        SoldierProduction soldierProduction = soldierProductionRepository.findById(soldierProductionId).orElseThrow();

        IslandMilitary islandMilitary = soldierProduction.getIslandMilitary();
        islandMilitary.addSoldierWithSoldierSubTypeEnum(soldierProduction.getSoldierSubType());
        islandMilitaryRepository.save(islandMilitary);

        soldierProduction.setSoldierCount(soldierProduction.getSoldierCount() - 1);
        soldierProduction.setActualStartTimestamp(System.currentTimeMillis());
        SoldierProduction recordedSoldierProduction = soldierProductionRepository.save(soldierProduction);

        if (soldierProduction.getSoldierCount() > 0) {
            rabbitmqService.sendSoldierProductionEndMessage(soldierProduction.getId(), soldierProduction.getProductionDuration().toMillis());

            sendToClient(recordedSoldierProduction.getIslandMilitary());
        } else {
            islandMilitary.removeSoldierProduction(soldierProduction.getId());
            IslandMilitary recordedIslandMilitary = islandMilitaryRepository.save(islandMilitary);

            Optional<List<SoldierSubTypeEnum>> sameSoldierTypesOptional = this.soldierTypeList
                    .keySet()
                    .stream()
                    .filter(e -> e.stream().anyMatch(se -> se.equals(soldierProduction.getSoldierSubType())))
                    .findFirst();

            Optional<SoldierProduction> soldierProductionNextOptional = islandMilitary.getSoldierProductionList().stream()
                    .filter(soldierProductionFind -> sameSoldierTypesOptional.get().contains(soldierProductionFind.getSoldierSubType()))
                    .findFirst();

            if(soldierProductionNextOptional.isPresent()) {
                soldierProductionNextOptional.get().setActualStartTimestamp(System.currentTimeMillis());
                soldierProductionNextOptional.get().setAllActualStartTimeStamp(System.currentTimeMillis());
                soldierProductionRepository.save(soldierProductionNextOptional.get());
                rabbitmqService.sendSoldierProductionEndMessage(soldierProductionNextOptional.get().getId(),
                        soldierProductionNextOptional.get().getProductionDuration().toMillis());
            }

            sendToClient(recordedIslandMilitary);

        }
    }

    public void sendToClient(IslandMilitary islandMilitary) {
        try {
            militaryGatewayClient.islandMilitaryChange(islandMilitary.getServerId(), islandMilitary.getUserId(), islandMilitary);
        } catch (Exception e) {
        }
    }

}
