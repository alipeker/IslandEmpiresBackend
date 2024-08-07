package com.islandempires.resourcesservice.service.privates;

import com.islandempires.resourcesservice.dto.RawMaterialsDTO;
import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import com.islandempires.resourcesservice.dto.request.LootingResourcesRequestDTO;
import com.islandempires.resourcesservice.dto.request.ResourceAllocationRequestDTO;
import com.islandempires.resourcesservice.model.IslandResource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public interface IslandResourceInteractionPrivateService {

    Mono<Boolean> validateResourceAllocationForIsland(String islandId, ResourceAllocationRequestDTO resourceAllocationRequestDTO);

    Mono<IslandResourceDTO> initializeIslandResource(String serverId, String islandId, IslandResourceDTO initialIslandResorceDTO, Long userId);

    Mono<IslandResourceDTO> assignResources(String islandId, ResourceAllocationRequestDTO resourceAllocationRequestDTO);
    Mono<IslandResourceDTO> refundResources(String islandId, ResourceAllocationRequestDTO resourceAllocationRequestDTO);

    Mono<Tuple2<IslandResource, IslandResource>> looting(String plunderedIslandId, String raidingIslandId, LootingResourcesRequestDTO lootingResourcesRequestDTO);

    Mono<Tuple2<IslandResourceDTO, IslandResourceDTO>> mutualTrading(String island1Id, String island2Id, RawMaterialsDTO island1TradingRawMaterials, RawMaterialsDTO island2TradingRawMaterials);

    Flux<IslandResource> addTest();
}