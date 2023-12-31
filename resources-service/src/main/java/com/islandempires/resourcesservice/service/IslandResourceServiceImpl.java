package com.islandempires.resourcesservice.service;

import com.islandempires.resourcesservice.dto.initial.IslandInitialResourceDTO;
import com.islandempires.resourcesservice.dto.request.IsResourcesEnoughControl;
import com.islandempires.resourcesservice.model.IslandResource;
import com.islandempires.resourcesservice.repository.IslandResourceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class IslandResourceServiceImpl implements IslandResourceService{
    @Autowired
    private IslandResourceRepository islandResourceRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Flux<IslandResource> getAll() {
        return this.islandResourceRepository.findAll();
    }

    @Override
    public Mono<IslandResource> get(String islandId) {
        return this.islandResourceRepository.findById(islandId);
    }

    @Override
    public Mono<IslandResource> prepareIslandInitialResource(IslandInitialResourceDTO islandInitialResourceDTO) {
        IslandResource islandResource = modelMapper.map(islandInitialResourceDTO, IslandResource.class);
        islandResource.setLastCalculatedTimestamp(System.currentTimeMillis());
        return islandResourceRepository.save(islandResource);
    }

    @Override
    public Mono<IslandResource> updateIslandResource(IslandResource islandResource) {
        return this.islandResourceRepository.findById(islandResource.getIslandid())
                .map(updatedResource -> {
                    updatedResource.setClay(islandResource.getClay());
                    return updatedResource;
                })
                .flatMap(this.islandResourceRepository::save);
    }

    @Override
    public Mono<Boolean> isResourcesEnoughControl(String islandId, IsResourcesEnoughControl isResourcesEnoughControl){
        return this.islandResourceRepository.findById(islandId)
                .map(islandResource -> {
                    return islandResource.isResourcesEnoughControl(isResourcesEnoughControl);
                });
    }

    public Flux<IslandResource> addTest() {
        List<IslandResource> islands = new ArrayList<>();

        for(int i=0; i<10000; i++) {
            IslandResource island1 = new IslandResource();
            island1.setIslandid("test".concat(i+""));
            island1.setWood(100.0);
            island1.setIron(200.0);
            island1.setClay(300.0);
            island1.setGold(0.0);
            island1.setRawMaterialStorageSize(1000);
            island1.setHourlyWoodProduction(50);
            island1.setHourlyIronProduction(60);
            island1.setHourlyClayProduction(70);
            island1.setHourlyGoldProduction(10);
            island1.setMeat(200.0);
            island1.setFish(150.0);
            island1.setWheat(100.0);
            island1.setFoodStorageSize(1000);
            island1.setPopulation(10.0);
            island1.setHourlyPopulationGrowth(50);
            island1.setPopulationLimit(2000);
            island1.setLastCalculatedTimestamp(System.currentTimeMillis());
            islands.add(island1);
        }
        return this.islandResourceRepository.saveAll(islands);
    }

}
