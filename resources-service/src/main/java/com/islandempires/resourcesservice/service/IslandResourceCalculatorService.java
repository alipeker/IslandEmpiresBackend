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
import com.islandempires.resourcesservice.model.IslandResource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IslandResourceCalculatorService {

    public Boolean checkResourceAllocation(IslandResource islandResourceDTO, ResourceAllocationRequestDTO resourceAllocationRequestDTO) {
        return  islandResourceDTO.getWood() >= resourceAllocationRequestDTO.getWood() &&
                islandResourceDTO.getClay() >= resourceAllocationRequestDTO.getClay() &&
                islandResourceDTO.getIron() >= resourceAllocationRequestDTO.getIron() &&
                islandResourceDTO.getPopulationLimit() >= islandResourceDTO.getPopulation() + resourceAllocationRequestDTO.getPopulation()
                ? true: false;
    }

    public Boolean checkRawMaterialsAllocation(IslandResourceDTO islandResourceDTO, RawMaterialsDTO allocationRawMaterialsDTO) {
        return  islandResourceDTO.getWood() >= allocationRawMaterialsDTO.getWood() &&
                islandResourceDTO.getClay() >= allocationRawMaterialsDTO.getClay() &&
                islandResourceDTO.getIron() >= allocationRawMaterialsDTO.getIron()
                ? true: false;
    }

    public IslandResource calculateResourceAllocation(IslandResource islandResource,
                                                      ResourceAllocationRequestDTO resourceAllocationRequestDTO) {
        if(!checkResourceAllocation(islandResource, resourceAllocationRequestDTO)) {
            throw new CustomRunTimeException(ExceptionE.INSUFFICIENT_RESOURCES);
        }

        Integer wood = resourceAllocationRequestDTO.getWood();
        Integer iron = resourceAllocationRequestDTO.getIron();
        Integer clay = resourceAllocationRequestDTO.getClay();
        Integer population = resourceAllocationRequestDTO.getPopulation();

        islandResource.setWood(islandResource.getWood() - wood);
        islandResource.setIron(islandResource.getIron() - iron);
        islandResource.setClay(islandResource.getClay() - clay);
        islandResource.setPopulation(islandResource.getPopulation() + population);

        return  islandResource;
    }

    public IslandResource refundResources(IslandResource islandResource,
                                                      ResourceAllocationRequestDTO resourceAllocationRequestDTO) {
        Integer rawMaterialStorageSize = islandResource.getRawMaterialStorageSize();

        Integer wood = resourceAllocationRequestDTO.getWood();
        Integer iron = resourceAllocationRequestDTO.getIron();
        Integer clay = resourceAllocationRequestDTO.getClay();
        Integer population = resourceAllocationRequestDTO.getPopulation();

        Double totalWood = islandResource.getWood() + wood > rawMaterialStorageSize ? rawMaterialStorageSize : islandResource.getWood() + wood;
        Double totalIron = islandResource.getIron() + iron > rawMaterialStorageSize ? rawMaterialStorageSize : islandResource.getIron() + wood;
        Double totalClay = islandResource.getClay() + clay > rawMaterialStorageSize ? rawMaterialStorageSize : islandResource.getClay() + wood;
        Integer totalPopulation = islandResource.getPopulation() - population <= 0 ? 0 : islandResource.getPopulation() - population;


        islandResource.setWood(totalWood);
        islandResource.setIron(totalIron);
        islandResource.setClay(totalClay);
        islandResource.setPopulation(totalPopulation);
        return  islandResource;
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
