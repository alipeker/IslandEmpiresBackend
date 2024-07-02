package com.islandempires.militaryservice.service;

import com.islandempires.militaryservice.dto.IslandResourceDTO;
import com.islandempires.militaryservice.enums.SoldierSubTypeEnum;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SoldierService {

    private final IslandMilitaryRepository islandMilitaryRepository;


    private final SoldierProductionRepository soldierProductionRepository;

    private final MilitaryGatewayClient militaryGatewayClient;

    private final RabbitmqService rabbitmqService;

    @Transactional
    public IslandResourceDTO createSoldier(String islandId, Long userId, SoldierSubTypeEnum soldierSubTypeEnum, int soldierCount) {
        IslandMilitary islandMilitary = islandMilitaryRepository.findById(islandId).orElseThrow();
        if(!islandMilitary.getUserId().equals(userId) || islandMilitary.getSoldierProductionList().size() > 3) {
            throw new RuntimeException();
        }
        Soldier soldier = islandMilitary.getStationaryTroops().getMilitaryUnits().getSoldiersWithSoldierSubTypeEnum(soldierSubTypeEnum);

        RawMaterialsAndPopulationCost rawMaterialsAndPopulationCost = null;
        try {
            rawMaterialsAndPopulationCost = (RawMaterialsAndPopulationCost) soldier.getSoldierBaseInfo().getRawMaterialsAndPopulationCost().clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        rawMaterialsAndPopulationCost.setId(null);
        Duration duration = soldier.getSoldierBaseInfo().getProductionDuration();

        SoldierProduction soldierProduction = new SoldierProduction();
        soldierProduction.setSoldierCount(soldierCount);
        soldierProduction.setSoldierSubType(soldierSubTypeEnum);
        soldierProduction.setProductionDuration(duration);
        soldierProduction.setRawMaterialsAndPopulationCost(rawMaterialsAndPopulationCost);
        soldierProduction.setIslandMilitaryId(islandMilitary.getIslandId());
        soldierProduction.setTime(LocalDateTime.now());
        SoldierProduction savedSoldierProduction = soldierProductionRepository.save(soldierProduction);


        RawMaterialsAndPopulationCost rawMaterialsAndPopulationCostAll = soldier.getSoldierBaseInfo().getRawMaterialsAndPopulationCost().multiply(soldierCount);
        IslandResourceDTO islandResourceDTO = militaryGatewayClient.assignResources(islandId, rawMaterialsAndPopulationCostAll);

        rabbitmqService.sendSoldierProductionEndMessage(savedSoldierProduction.getId(), Duration.ofSeconds(30).toMillis());
        return islandResourceDTO;
    }


    @Transactional
    public IslandResourceDTO cancelSoldierProduction(Long soldierProductionId, Long userId) {
        SoldierProduction soldierProduction = soldierProductionRepository.findById(soldierProductionId).orElseThrow();

        if(!soldierProduction.getIslandMilitary().getUserId().equals(userId) || soldierProduction.getSoldierCount() <= 1) {
            throw new RuntimeException();
        }

        int soldierCount = soldierProduction.getSoldierCount() - 1;
        soldierProduction.setSoldierCount(1);
        soldierProductionRepository.save(soldierProduction);
        return militaryGatewayClient.refundResources(soldierProduction.getIslandMilitaryId(), soldierProduction.getRawMaterialsAndPopulationCost().multiply(soldierCount));
    }

    @RabbitListener(queues = RabbitMqConfig.SOLDIER_CRATE_QUEUE_NAME)
    @Transactional
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
        soldierProductionRepository.save(soldierProduction);

        if(soldierProduction.getSoldierCount() > 0) {
            rabbitmqService.sendSoldierProductionEndMessage(soldierProduction.getId(), Duration.ofSeconds(30).toMillis());
        } else {
            soldierProduction.setIsActive(false);
            soldierProduction.setRawMaterialsAndPopulationCost(null);
            soldierProductionRepository.save(soldierProduction);
        }
    }

}
