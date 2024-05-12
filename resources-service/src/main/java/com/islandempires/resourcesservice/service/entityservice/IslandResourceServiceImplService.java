package com.islandempires.resourcesservice.service.entityservice;

import com.islandempires.resourcesservice.dto.RawMaterialsDTO;
import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import com.islandempires.resourcesservice.dto.request.LootingResourcesRequestDTO;
import com.islandempires.resourcesservice.dto.request.ResourceAllocationRequestDTO;
import com.islandempires.resourcesservice.enums.IslandResourceEnum;
import com.islandempires.resourcesservice.enums.MutualTradingEnum;
import com.islandempires.resourcesservice.enums.PlunderedRaidingEnum;
import com.islandempires.resourcesservice.exception.CustomRunTimeException;
import com.islandempires.resourcesservice.exception.ExceptionE;
import com.islandempires.resourcesservice.model.IslandResource;
import com.islandempires.resourcesservice.repository.IslandResourceRepository;
import com.islandempires.resourcesservice.service.IslandMetricsCalculatorService;
import com.islandempires.resourcesservice.service.IslandResourceCalculatorService;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import reactor.util.function.Tuple2;

@Service
public class IslandResourceServiceImplService implements IslandResourceQueryService, IslandResourceModificationService, IslandResourceInteractionService {
    @Autowired
    private IslandResourceRepository islandResourceRepository;

    @Autowired
    private IslandResourceCalculatorService islandResourceCalculatorService;

    @Autowired
    private IslandMetricsCalculatorService islandMetricsCalculatorService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Flux<IslandResourceDTO> getAll() {
        return islandResourceRepository.findAll().map(islandResource -> modelMapper.map(islandResource, IslandResourceDTO.class));
    }

    @Override
    public Mono<IslandResourceDTO> get(String islandId, Long userId) {
        return this.islandResourceRepository.findById(islandId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .map(islandResource -> {
                    if(userId != islandResource.getUserId()) {
                        throw new CustomRunTimeException(ExceptionE.ISLAND_PRIVILEGES);
                    }
                    return modelMapper.map(islandResource, IslandResourceDTO.class);
                });
    }

    @Override
    public Mono<Void> delete(String islandId) {
        return islandResourceRepository.deleteByIslandId(islandId)
                .doOnError(error -> {
                    throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
                });
    }

    @Override
    public Mono<IslandResourceDTO> initializeIslandResource(IslandResourceDTO islandResourceDTO, Long userid) {
        if(islandResourceDTO.getIslandId() != null) {
            islandResourceRepository.findById(islandResourceDTO.getIslandId()).doOnNext(result -> {
                if (result != null) {
                    throw new CustomRunTimeException(ExceptionE.ALREADY_EXIST);
                }});
        }

        islandResourceDTO = islandMetricsCalculatorService.calculateIslandResourceFields(islandResourceDTO);
        IslandResource islandResource = modelMapper.map(islandResourceDTO, IslandResource.class);
        islandResource.setLastCalculatedTimestamp(System.currentTimeMillis());
        islandResource.setCreatedDate(LocalDateTime.now());
        islandResource.setUserId(userid);

        return islandResourceRepository.save(islandResource).map(savedIslandResource -> modelMapper.map(savedIslandResource, IslandResourceDTO.class));
    }

    @Override
    public Mono<Boolean> validateResourceAllocationForIsland(String islandId, ResourceAllocationRequestDTO resourceAllocationRequestDTO, Long userId){
        return islandResourceRepository.findById(islandId)
                .map(islandResource -> {
                    if(userId != islandResource.getUserId()) {
                        throw new CustomRunTimeException(ExceptionE.ISLAND_PRIVILEGES);
                    }
                    return islandResourceCalculatorService.checkResourceAllocation(modelMapper.map(islandResource, IslandResourceDTO.class),
                            resourceAllocationRequestDTO);
                }).switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)));
    }

    @Override
    public Mono<IslandResourceDTO> assignResources(String islandId, ResourceAllocationRequestDTO resourceAllocationRequestDTO, Long userId){
        return islandResourceRepository.findById(islandId)
                .map(islandResource -> {
                    if(islandResource.getUserId() != userId) {
                        throw new CustomRunTimeException(ExceptionE.ISLAND_PRIVILEGES);
                    }
                    return modelMapper.map(
                            islandResourceCalculatorService.calculateResourceAllocation(
                                    modelMapper.map(islandResource, IslandResourceDTO.class),
                                    resourceAllocationRequestDTO), IslandResource.class);
                }).flatMap(islandResourceRepository::save).switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .map(islandResource -> modelMapper.map(islandResource, IslandResourceDTO.class));
    }

    @Override
    public Mono<Tuple2<IslandResource, IslandResource>> looting(String plunderedIslandId, String raidingIslandId, LootingResourcesRequestDTO lootingResourcesRequestDTO){
        Mono<IslandResource> plunderedIslandResourceMono = islandResourceRepository.findById(plunderedIslandId).switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)));
        Mono<IslandResource> raidingIslandResourceMono = islandResourceRepository.findById(raidingIslandId).switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)));

        return Mono.zip(plunderedIslandResourceMono, raidingIslandResourceMono).flatMap(tuple -> {
            IslandResource plunderedIslandResource = tuple.getT1();
            IslandResource raidingIslandResource = tuple.getT2();

            IslandResourceDTO senderIslandResourceDTO =  modelMapper.map(plunderedIslandResource, IslandResourceDTO.class);
            IslandResourceDTO receiverIslandResourceDTO =  modelMapper.map(raidingIslandResource, IslandResourceDTO.class);

            Map<PlunderedRaidingEnum,IslandResourceDTO> senderAndReceiverIslandResourcesDTO
                    = islandResourceCalculatorService.calculateLooting(senderIslandResourceDTO, receiverIslandResourceDTO, lootingResourcesRequestDTO);

            return Mono.zip(
                    islandResourceRepository.save(modelMapper.map(senderAndReceiverIslandResourcesDTO.get(PlunderedRaidingEnum.PLUNDERED), IslandResource.class)),
                    islandResourceRepository.save(modelMapper.map(senderAndReceiverIslandResourcesDTO.get(PlunderedRaidingEnum.RAIDING), IslandResource.class))
            );
        });
    }

    @Override
    public Mono<Tuple2<IslandResourceDTO, IslandResourceDTO>> mutualTrading(String island1Id, String island2Id, RawMaterialsDTO island1TradingRawMaterials, RawMaterialsDTO island2TradingRawMaterials) {
        Mono<IslandResource> island1IslandResourceMono = islandResourceRepository.findById(island1Id).switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)));
        Mono<IslandResource> island2IslandResourceMono = islandResourceRepository.findById(island2Id).switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)));


        return Mono.zip(island1IslandResourceMono, island2IslandResourceMono).flatMap(tuple -> {
            IslandResource senderIslandResource = tuple.getT1();
            IslandResource receiverIslandResource = tuple.getT2();

            IslandResourceDTO senderIslandResourceDTO =  modelMapper.map(senderIslandResource, IslandResourceDTO.class);
            IslandResourceDTO receiverIslandResourceDTO =  modelMapper.map(receiverIslandResource, IslandResourceDTO.class);

            Map<MutualTradingEnum,IslandResourceDTO> senderAndReceiverIslandResourcesDTO
                    = islandResourceCalculatorService.calculateMutualTrading(senderIslandResourceDTO, receiverIslandResourceDTO, island1TradingRawMaterials, island2TradingRawMaterials);

            return Mono.zip(
                    islandResourceRepository.save(modelMapper.map(senderAndReceiverIslandResourcesDTO.get(MutualTradingEnum.ISLAND1), IslandResource.class))
                            .map(savedIslandResource -> modelMapper.map(savedIslandResource, IslandResourceDTO.class)),
                    islandResourceRepository.save(modelMapper.map(senderAndReceiverIslandResourcesDTO.get(MutualTradingEnum.ISLAND2), IslandResource.class))
                            .map(savedIslandResource -> modelMapper.map(savedIslandResource, IslandResourceDTO.class))
            );
        });
    }

    @Override
    public Mono<IslandResourceDTO> updateIslandResourceField(String islandId, IslandResourceEnum islandResourceEnum, Number newValue) {
        return islandResourceRepository.findById(islandId).switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND))).map(islandResource -> {
            switch (islandResourceEnum) {
                case WOOD_HOURLY_PRODUCTION:
                    islandResource.setWoodHourlyProduction(newValue.intValue());
                    break;
                case IRON_HOURLY_PRODUCTION:
                    islandResource.setIronHourlyProduction(newValue.intValue());
                    break;
                case CLAY_HOURLY_PRODUCTION:
                    islandResource.setClayHourlyProduction(newValue.intValue());
                    break;
                case RAW_MATERIAL_STORAGE_SIZE:
                    islandResource.setRawMaterialStorageSize(newValue.intValue());
                    break;
                case MEAT_HOURLY_PRODUCTION:
                    islandResource.setMeatHourlyProduction(newValue.intValue());
                    islandResource = modelMapper.map(islandMetricsCalculatorService.calculateIslandResourceFields(
                            modelMapper.map(islandResource, IslandResourceDTO.class)), IslandResource.class);
                    break;
                case FISH_HOURLY_PRODUCTION:
                    islandResource.setFishHourlyProduction(newValue.intValue());
                    islandResource = modelMapper.map(islandMetricsCalculatorService.calculateIslandResourceFields(
                            modelMapper.map(islandResource, IslandResourceDTO.class)), IslandResource.class);
                    break;
                case WHEAT_HOURLY_PRODUCTION:
                    islandResource.setWheatHourlyProduction(newValue.intValue());
                    islandResource = modelMapper.map(islandMetricsCalculatorService.calculateIslandResourceFields(
                            modelMapper.map(islandResource, IslandResourceDTO.class)), IslandResource.class);
                    break;
                case ADDITIONAL_HAPPINESS_SCORE:
                    islandResource.setAdditionalHappinessScore(newValue.doubleValue());
                    islandResource = modelMapper.map(islandMetricsCalculatorService.calculateHappinessScore(
                            modelMapper.map(islandResource, IslandResourceDTO.class)), IslandResource.class);
                    break;
                default:
                    throw new CustomRunTimeException(ExceptionE.ENUM_NOT_FOUND);
            }
            return islandResource;})
                .flatMap(updatedResource -> islandResourceRepository.save(updatedResource))
                .map(islandResource -> modelMapper.map(islandResource, IslandResourceDTO.class));
    }

    @Override
    public Mono<IslandResourceDTO> increaseOrDecreaseIslandResourceField(String islandId, IslandResourceEnum islandResourceEnum, Number value) {
        return islandResourceRepository.findById(islandId).switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND))).map(islandResource -> {
            switch (islandResourceEnum) {
                        case GOLD:
                            Double totalGold = islandResource.getGold() + value.doubleValue();
                            islandResource.setGold(totalGold < 0 ? 0 : totalGold);
                            break;
                        case POPULATION:
                            Integer totalPopulation = islandResource.getPopulation() + value.intValue();
                            islandResource.setPopulation(totalPopulation < 0 ? 0 : totalPopulation);
                            islandResource = modelMapper.map(islandMetricsCalculatorService.calculateIslandResourceFields(
                                    modelMapper.map(islandResource, IslandResourceDTO.class)), IslandResource.class);
                            break;
                        case TEMPORARY_POPULATION:
                            Integer totalTemporaryPopulation = islandResource.getTemporaryPopulation() + value.intValue();
                            islandResource.setTemporaryPopulation(totalTemporaryPopulation);
                            islandResource = modelMapper.map(islandMetricsCalculatorService.calculateIslandResourceFields(
                                    modelMapper.map(islandResource, IslandResourceDTO.class)), IslandResource.class);
                            break;
                        default:
                            throw new CustomRunTimeException(ExceptionE.ENUM_NOT_FOUND);
                    }
                    return islandResource;})
                .flatMap(updatedResource -> islandResourceRepository.save(updatedResource))
                .map(islandResource -> modelMapper.map(islandResource, IslandResourceDTO.class));
    }

    @Override
    public Flux<IslandResource> addTest() {
        List<IslandResource> islands = new ArrayList<>();

        Random random = new Random();
        LocalDateTime timestampNow = LocalDateTime.now();


        for(int i=0; i<10000; i++) {
            IslandResource island1 = new IslandResource();
            island1.setId("testresource".concat(i+""));
            island1.setIslandId("test".concat(i+""));
            island1.setWood(Double.valueOf(random.nextInt(200)));
            island1.setIron(Double.valueOf(random.nextInt(200)));
            island1.setClay(Double.valueOf(random.nextInt(200)));
            island1.setGold(Double.valueOf(random.nextInt(200)));
            island1.setRawMaterialStorageSize(random.nextInt(500 - 201) + 201);
            island1.setWoodHourlyProduction(random.nextInt(100 - 70) + 70);
            island1.setIronHourlyProduction(random.nextInt(100 - 60) + 60);
            island1.setClayHourlyProduction(random.nextInt(100 - 60) + 60);

            island1.setMeatFoodCoefficient(200.0);
            island1.setMeatHourlyProduction(150);
            island1.setFishFoodCoefficient(200.0);
            island1.setFishHourlyProduction(150);
            island1.setWheatFoodCoefficient(200.0);
            island1.setWheatHourlyProduction(150);

            island1.setPopulation(random.nextInt(100 - 60) + 60);
            island1.setTemporaryPopulation(random.nextInt(3 - 0) + 0);
            island1.setPopulationLimit(random.nextInt(200 - 63) + 63);

            island1.setAdditionalHappinessScore(0.0);

            island1.setLastCalculatedTimestamp(System.currentTimeMillis());
            island1.setCreatedDate(timestampNow);

            IslandResourceDTO islandResourceDTO = modelMapper.map(island1, IslandResourceDTO.class);
            island1 = modelMapper.map(islandMetricsCalculatorService.calculateIslandResourceFields(islandResourceDTO), IslandResource.class);
            islands.add(island1);
        }
        return this.islandResourceRepository.saveAll(islands);
    }

}
