package com.islandempires.resourcesservice.service;

import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import com.islandempires.resourcesservice.dto.PopulationDTO;
import com.islandempires.resourcesservice.dto.RawMaterialsDTO;
import com.islandempires.resourcesservice.dto.request.LootingResourcesRequestDTO;
import com.islandempires.resourcesservice.dto.request.ResourceAllocationRequestDTO;
import com.islandempires.resourcesservice.enums.MutualTradingEnum;
import com.islandempires.resourcesservice.enums.PlunderedRaidingEnum;
import com.islandempires.resourcesservice.enums.RawMaterialEnum;
import com.islandempires.resourcesservice.exception.CustomRunTimeException;
import com.islandempires.resourcesservice.exception.ExceptionE;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IslandResourceCalculatorService {

    public Boolean checkResourceAllocation(IslandResourceDTO islandResourceDTO, ResourceAllocationRequestDTO resourceAllocationRequestDTO) {
        RawMaterialsDTO resourceAllocationRequestRawMaterialsDTO = resourceAllocationRequestDTO.getRawMaterials();
        PopulationDTO resourceAllocationRequestPopulationDTO = resourceAllocationRequestDTO.getPopulation();
        return  islandResourceDTO.getWood() >= resourceAllocationRequestRawMaterialsDTO.getWood() &&
                islandResourceDTO.getClay() >= resourceAllocationRequestRawMaterialsDTO.getClay() &&
                islandResourceDTO.getIron() >= resourceAllocationRequestRawMaterialsDTO.getIron() &&
                islandResourceDTO.getPopulationLimit() >= islandResourceDTO.getPopulation() + resourceAllocationRequestPopulationDTO.getPopulation()
                ? true: false;
    }

    public Boolean checkRawMaterialsAllocation(IslandResourceDTO islandResourceDTO, RawMaterialsDTO allocationRawMaterialsDTO) {
        return  islandResourceDTO.getWood() >= allocationRawMaterialsDTO.getWood() &&
                islandResourceDTO.getClay() >= allocationRawMaterialsDTO.getClay() &&
                islandResourceDTO.getIron() >= allocationRawMaterialsDTO.getIron()
                ? true: false;
    }

    public IslandResourceDTO calculateResourceAllocation(IslandResourceDTO islandResourceDTO,
                                                         ResourceAllocationRequestDTO resourceAllocationRequestDTO) {
        if(!checkResourceAllocation(islandResourceDTO, resourceAllocationRequestDTO)) {
            throw new CustomRunTimeException(ExceptionE.INSUFFICIENT_RESOURCES);
        }
        RawMaterialsDTO rawMaterialsDTO = resourceAllocationRequestDTO.getRawMaterials();
        PopulationDTO populationDTO = resourceAllocationRequestDTO.getPopulation();

        Integer wood = rawMaterialsDTO.getWood();
        Integer iron = rawMaterialsDTO.getIron();
        Integer clay = rawMaterialsDTO.getClay();
        Integer population = populationDTO.getPopulation();

        islandResourceDTO.setWood(islandResourceDTO.getWood() - wood);
        islandResourceDTO.setIron(islandResourceDTO.getIron() - iron);
        islandResourceDTO.setClay(islandResourceDTO.getClay() - clay);
        islandResourceDTO.setPopulation(islandResourceDTO.getPopulation() + population);

        return  islandResourceDTO;
    }

    public Map<RawMaterialEnum, Integer> randomLootingRawMaterials(Map<RawMaterialEnum, Integer> plunderedIslandRawMaterials, Map<RawMaterialEnum, Integer> calculatedLootingRawMaterialMap,
                                                                   Integer lootingSize) {
        List<RawMaterialEnum> rawMaterialList = new ArrayList(calculatedLootingRawMaterialMap.keySet());
        Collections.shuffle(rawMaterialList);

        int averageLootingSize = lootingSize / 3;
        int remainder = lootingSize - (averageLootingSize * 3);

        for(RawMaterialEnum rawMaterialEnum : rawMaterialList) {
            if(averageLootingSize > plunderedIslandRawMaterials.get(rawMaterialEnum)) {
                remainder += averageLootingSize - plunderedIslandRawMaterials.get(rawMaterialEnum);
            }
        }

        for(RawMaterialEnum rawMaterialEnum : rawMaterialList) {
            int calculatedLootingMaterial = 0;
            if(averageLootingSize > plunderedIslandRawMaterials.get(rawMaterialEnum)) {
                calculatedLootingMaterial = plunderedIslandRawMaterials.get(rawMaterialEnum);
            } else if(remainder > 0) {
                if(remainder + averageLootingSize <= plunderedIslandRawMaterials.get(rawMaterialEnum)) {
                    calculatedLootingMaterial = remainder + averageLootingSize;
                    remainder = 0;
                } else {
                    calculatedLootingMaterial = plunderedIslandRawMaterials.get(rawMaterialEnum);
                    remainder -= calculatedLootingMaterial - averageLootingSize;
                }
            } else {
                calculatedLootingMaterial = averageLootingSize;
            }
            calculatedLootingRawMaterialMap.replace(rawMaterialEnum, calculatedLootingMaterial);
        }
        return calculatedLootingRawMaterialMap;
    }

    public Map<PlunderedRaidingEnum,IslandResourceDTO> calculateLooting(IslandResourceDTO plunderedIslandResourcesDTO,
                                                                              IslandResourceDTO raidingIslandResourcesDTO,
                                                                              LootingResourcesRequestDTO lootingResourcesRequestDTO) {

        Integer lootingSize = lootingResourcesRequestDTO.getLootingSize();
        PopulationDTO lootingPopulationDTO = lootingResourcesRequestDTO.getPopulationDTO();

        Map<RawMaterialEnum, Integer> plunderedIslandRawMaterials = new HashMap<>();
        plunderedIslandRawMaterials.put(RawMaterialEnum.WOOD, plunderedIslandResourcesDTO.getWood());
        plunderedIslandRawMaterials.put(RawMaterialEnum.IRON, plunderedIslandResourcesDTO.getIron());
        plunderedIslandRawMaterials.put(RawMaterialEnum.CLAY, plunderedIslandResourcesDTO.getClay());

        Map<RawMaterialEnum, Integer> calculatedLootingRawMaterialMap = new HashMap<>();
        calculatedLootingRawMaterialMap.put(RawMaterialEnum.WOOD, 0);
        calculatedLootingRawMaterialMap.put(RawMaterialEnum.IRON, 0);
        calculatedLootingRawMaterialMap.put(RawMaterialEnum.CLAY, 0);

        if(lootingSize >= plunderedIslandRawMaterials.get(RawMaterialEnum.WOOD) + plunderedIslandRawMaterials.get(RawMaterialEnum.IRON) + plunderedIslandRawMaterials.get(RawMaterialEnum.CLAY) ) {
            calculatedLootingRawMaterialMap.replace(RawMaterialEnum.WOOD, plunderedIslandResourcesDTO.getWood());
            calculatedLootingRawMaterialMap.replace(RawMaterialEnum.IRON, plunderedIslandResourcesDTO.getIron());
            calculatedLootingRawMaterialMap.replace(RawMaterialEnum.CLAY, plunderedIslandResourcesDTO.getClay());
        } else {
            calculatedLootingRawMaterialMap = randomLootingRawMaterials(plunderedIslandRawMaterials, calculatedLootingRawMaterialMap, lootingSize);
        }
        Integer calculatedDecreasingPopulation = plunderedIslandResourcesDTO.getPopulation() > lootingPopulationDTO.getPopulation()
                ? lootingPopulationDTO.getPopulation() : plunderedIslandResourcesDTO.getPopulation();

        plunderedIslandResourcesDTO.setWood(plunderedIslandResourcesDTO.getWood() - calculatedLootingRawMaterialMap.get(RawMaterialEnum.WOOD));
        plunderedIslandResourcesDTO.setIron(plunderedIslandResourcesDTO.getIron() - calculatedLootingRawMaterialMap.get(RawMaterialEnum.IRON));
        plunderedIslandResourcesDTO.setClay(plunderedIslandResourcesDTO.getClay() - calculatedLootingRawMaterialMap.get(RawMaterialEnum.CLAY));
        plunderedIslandResourcesDTO.setPopulation(plunderedIslandResourcesDTO.getPopulation() - calculatedDecreasingPopulation);

        raidingIslandResourcesDTO.setWood(raidingIslandResourcesDTO.getWood() + calculatedLootingRawMaterialMap.get(RawMaterialEnum.WOOD));
        raidingIslandResourcesDTO.setIron(raidingIslandResourcesDTO.getIron() + calculatedLootingRawMaterialMap.get(RawMaterialEnum.IRON));
        raidingIslandResourcesDTO.setClay(raidingIslandResourcesDTO.getClay() + calculatedLootingRawMaterialMap.get(RawMaterialEnum.CLAY));

        Map<PlunderedRaidingEnum,IslandResourceDTO> senderAndReceiverIslandResourcesDTO = new HashMap<>();
        senderAndReceiverIslandResourcesDTO.put(PlunderedRaidingEnum.PLUNDERED, plunderedIslandResourcesDTO);
        senderAndReceiverIslandResourcesDTO.put(PlunderedRaidingEnum.RAIDING, raidingIslandResourcesDTO);

        return senderAndReceiverIslandResourcesDTO;
    }

    public Map<MutualTradingEnum,IslandResourceDTO> calculateMutualTrading(IslandResourceDTO island1ResourcesDTO,
                                                                           IslandResourceDTO island2ResourcesDTO,
                                                                           RawMaterialsDTO island1TradingRawMaterials,
                                                                           RawMaterialsDTO island2TradingRawMaterials) {

        if(!checkRawMaterialsAllocation(island1ResourcesDTO, island1TradingRawMaterials) &&
           !checkRawMaterialsAllocation(island2ResourcesDTO, island2TradingRawMaterials)) {
            throw new CustomRunTimeException(ExceptionE.INSUFFICIENT_RESOURCES);
        }

        island1ResourcesDTO.setWood(island1ResourcesDTO.getWood() + (island2TradingRawMaterials.getWood() - island1TradingRawMaterials.getWood()));
        island1ResourcesDTO.setIron(island1ResourcesDTO.getIron() + (island2TradingRawMaterials.getIron() - island1TradingRawMaterials.getIron()));
        island1ResourcesDTO.setClay(island1ResourcesDTO.getClay() + (island2TradingRawMaterials.getClay() - island1TradingRawMaterials.getClay()));


        island2ResourcesDTO.setWood(island2ResourcesDTO.getWood() + (island1TradingRawMaterials.getWood() - island2TradingRawMaterials.getWood()));
        island2ResourcesDTO.setIron(island2ResourcesDTO.getIron() + (island1TradingRawMaterials.getIron() - island2TradingRawMaterials.getIron()));
        island2ResourcesDTO.setClay(island2ResourcesDTO.getClay() + (island1TradingRawMaterials.getClay() - island2TradingRawMaterials.getClay()));


        Map<MutualTradingEnum,IslandResourceDTO> senderAndReceiverIslandResourcesDTO = new HashMap<>();
        senderAndReceiverIslandResourcesDTO.put(MutualTradingEnum.ISLAND1, island1ResourcesDTO);
        senderAndReceiverIslandResourcesDTO.put(MutualTradingEnum.ISLAND2, island2ResourcesDTO);

        return senderAndReceiverIslandResourcesDTO;
    }

}
