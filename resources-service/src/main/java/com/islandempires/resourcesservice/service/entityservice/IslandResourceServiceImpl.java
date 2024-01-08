package com.islandempires.resourcesservice.service.entityservice;

import com.islandempires.resourcesservice.dto.RawMaterialsDTO;
import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import com.islandempires.resourcesservice.dto.request.LootingResourcesRequestDTO;
import com.islandempires.resourcesservice.dto.request.ResourceAllocationRequestDTO;
import com.islandempires.resourcesservice.enums.MutualTradingEnum;
import com.islandempires.resourcesservice.enums.PlunderedRaidingEnum;
import com.islandempires.resourcesservice.exception.CustomRunTimeException;
import com.islandempires.resourcesservice.exception.ExceptionE;
import com.islandempires.resourcesservice.model.IslandResource;
import com.islandempires.resourcesservice.repository.IslandResourceRepository;
import com.islandempires.resourcesservice.service.IslandResourceCalculatorService;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import reactor.util.function.Tuple2;

@Service
public class IslandResourceServiceImpl implements IslandResourceService{
    @Autowired
    private IslandResourceRepository islandResourceRepository;

    @Autowired
    private IslandResourceCalculatorService islandResourceCalculatorService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Flux<IslandResource> getAll() {
        return this.islandResourceRepository.findAll();
    }

    @Override
    public Mono<IslandResource> get(String islandId) {
        return this.islandResourceRepository.findById(islandId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)));
    }

    @Override
    public Mono<IslandResource> prepareIslandInitialResource(IslandResourceDTO islandResourceDTO) {
        islandResourceRepository.findById(islandResourceDTO.getIslandId()).doOnNext(result -> {
            if (result != null) {
                throw new CustomRunTimeException(ExceptionE.ALREADY_EXIST);
            }});

        IslandResource islandResource = modelMapper.map(islandResourceDTO, IslandResource.class);
        islandResource.setLastCalculatedTimestamp(System.currentTimeMillis());
        return islandResourceRepository.save(islandResource);
    }

    @Override
    public Mono<IslandResource> updateIslandResource(IslandResource islandResource) {
        return islandResourceRepository.findById(islandResource.getIslandId())
                .map(updatedResource -> {
                    updatedResource.setClay(islandResource.getClay());
                    return updatedResource;
                }).flatMap(islandResourceRepository::save);
    }

    @Override
    public Mono<Boolean> checkResourceAllocation(String islandId, ResourceAllocationRequestDTO resourceAllocationRequestDTO){
        return islandResourceRepository.findById(islandId)
                .map(islandResource -> {
                    return islandResourceCalculatorService.checkResourceAllocation(modelMapper.map(islandResource, IslandResourceDTO.class),
                            resourceAllocationRequestDTO);
                }).switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)));
    }

    @Override
    public Mono<IslandResource> assignResources(String islandId, ResourceAllocationRequestDTO resourceAllocationRequestDTO){
        return islandResourceRepository.findById(islandId)
                .map(islandResource -> {
                    return modelMapper.map(
                            islandResourceCalculatorService.calculateResourceAllocation(
                                    modelMapper.map(islandResource, IslandResourceDTO.class),
                                    resourceAllocationRequestDTO), IslandResource.class);
                }).flatMap(islandResourceRepository::save).switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)));
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
    public Mono<Tuple2<IslandResource, IslandResource>> mutualTrading(String island1Id, String island2Id, RawMaterialsDTO island1TradingRawMaterials, RawMaterialsDTO island2TradingRawMaterials) {
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
                    islandResourceRepository.save(modelMapper.map(senderAndReceiverIslandResourcesDTO.get(MutualTradingEnum.ISLAND1), IslandResource.class)),
                    islandResourceRepository.save(modelMapper.map(senderAndReceiverIslandResourcesDTO.get(MutualTradingEnum.ISLAND2), IslandResource.class))
            );
        });
    }

    @Override
    public Mono<Void> increaseWoodHourlyProduction(String islandId, Integer newWoodHourlyProduction) {
        return islandResourceRepository.findById(islandId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .flatMap(islandResource -> {
                    islandResource.setWoodHourlyProduction(newWoodHourlyProduction);
                    return islandResourceRepository.save(islandResource).then();
                }).thenEmpty(Mono.empty());
    }

    @Override
    public Mono<Void> increaseIronHourlyProduction(String islandId, Integer newIronHourlyProduction) {
        return islandResourceRepository.findById(islandId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .flatMap(islandResource -> {
                    islandResource.setIronHourlyProduction(newIronHourlyProduction);
                    return islandResourceRepository.save(islandResource).then();
                }).thenEmpty(Mono.empty());
    }

    @Override
    public Mono<Void> increaseClayHourlyProduction(String islandId, Integer newClayHourlyProduction) {
        return islandResourceRepository.findById(islandId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .flatMap(islandResource -> {
                    islandResource.setClayHourlyProduction(newClayHourlyProduction);
                    return islandResourceRepository.save(islandResource).then();
                });
    }

    @Override
    public Mono<Void> increaseMeatHourlyProduction(String islandId, Integer newMeatHourlyProduction) {
        return islandResourceRepository.findById(islandId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .flatMap(islandResource -> {
                    islandResource.setMeatHourlyProduction(newMeatHourlyProduction);
                    return islandResourceRepository.save(islandResource).then();
                });
    }

    @Override
    public Mono<Void> increaseFishHourlyProduction(String islandId, Integer newFishHourlyProduction) {
        return islandResourceRepository.findById(islandId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .flatMap(islandResource -> {
                    islandResource.setFishHourlyProduction(newFishHourlyProduction);
                    return islandResourceRepository.save(islandResource).then();
                });
    }

    @Override
    public Mono<Void> increaseWheatHourlyProduction(String islandId, Integer newWheatHourlyProduction) {
        return islandResourceRepository.findById(islandId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .flatMap(islandResource -> {
                    islandResource.setWheatHourlyProduction(newWheatHourlyProduction);
                    return islandResourceRepository.save(islandResource).then();
                });
    }

    public Flux<IslandResource> addTest() {
        List<IslandResource> islands = new ArrayList<>();

        Random random = new Random();

        for(int i=0; i<10000; i++) {
            IslandResource island1 = new IslandResource();
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

            island1.setHappinessScore(random.nextDouble(1 - 0));
            island1.setAdditionalHappinessScore(0.0);

            island1.setLastCalculatedTimestamp(System.currentTimeMillis());
            islands.add(island1);
        }
        return this.islandResourceRepository.saveAll(islands);
    }

}
