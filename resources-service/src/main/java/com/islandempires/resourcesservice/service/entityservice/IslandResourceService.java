package com.islandempires.resourcesservice.service.entityservice;

import com.islandempires.resourcesservice.dto.RawMaterialsDTO;
import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import com.islandempires.resourcesservice.dto.request.LootingResourcesRequestDTO;
import com.islandempires.resourcesservice.dto.request.ResourceAllocationRequestDTO;
import com.islandempires.resourcesservice.model.IslandResource;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public interface IslandResourceService {
    Flux<IslandResource> getAll();

    Mono<IslandResource> get(String islandId);

    Mono<Boolean> checkResourceAllocation(String islandId, ResourceAllocationRequestDTO resourceAllocationRequestDTO);

    Mono<IslandResource> prepareIslandInitialResource(IslandResourceDTO initialIslandResorceDTO);

    Mono<IslandResource> updateIslandResource(IslandResource islandResource);

    Mono<IslandResource> assignResources(String islandId, ResourceAllocationRequestDTO resourceAllocationRequestDTO);

    Mono<Tuple2<IslandResource, IslandResource>> looting(String plunderedIslandId, String raidingIslandId, LootingResourcesRequestDTO lootingResourcesRequestDTO);

    Mono<Tuple2<IslandResource, IslandResource>> mutualTrading(String island1Id, String island2Id, RawMaterialsDTO island1TradingRawMaterials, RawMaterialsDTO island2TradingRawMaterials);

    Mono<Void> changeWoodHourlyProduction(String islandId, Integer newWoodHourlyProduction);

    Mono<Void> changeIronHourlyProduction(String islandId, Integer newIronHourlyProduction);

    Mono<Void> changeClayHourlyProduction(String islandId, Integer newClayHourlyProduction);

    Mono<Void> increaseOrDecreaseGold(String islandId, Integer gold);

    Mono<Void> changeRawMaterialStorageSize(String islandId, Integer newChangeRawMaterialStorageSize);

    Mono<Void> changeMeatHourlyProduction(String islandId, Integer newMeatHourlyProduction);

    Mono<Void> changeFishHourlyProduction(String islandId, Integer newFishHourlyProduction);

    Mono<Void> changeWheatHourlyProduction(String islandId, Integer newWheatHourlyProduction);

    Mono<Void> increaseOrDecreasePopulation(String islandId, Integer population);

    Mono<Void> increaseOrDecreaseTemporaryPopulation(String islandId, Integer population);

    Mono<Void> increaseOrDecreaseAdditionalHappinessScore(String islandId, Integer additionalHappinessScore);


    Flux<IslandResource> addTest();
}
