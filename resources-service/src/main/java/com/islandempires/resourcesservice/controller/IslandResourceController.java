package com.islandempires.resourcesservice.controller;

import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import com.islandempires.resourcesservice.dto.request.IncreaseResourceRequest;
import com.islandempires.resourcesservice.dto.request.LootingResourcesRequestDTO;
import com.islandempires.resourcesservice.dto.request.MutualTradingRequestDTO;
import com.islandempires.resourcesservice.dto.request.ResourceAllocationRequestDTO;
import com.islandempires.resourcesservice.model.IslandResource;
import com.islandempires.resourcesservice.service.entityservice.IslandResourceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@CrossOrigin
@RestController
@RequestMapping("/resource")
public class IslandResourceController {

    @Autowired
    private IslandResourceService islandResourceService;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<IslandResource> streamResources() {
        return this.islandResourceService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{islandid}")
    public Mono<IslandResource> get(@PathVariable String islandid) {
        return islandResourceService.get(islandid);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/checkResourceAllocation/{islandid}")
    public Mono<Boolean> checkResourceAllocation(@Size(min = 1, message = "Id must be not empty") @PathVariable String islandId,
                                                 @Valid @RequestBody ResourceAllocationRequestDTO resourceAllocationRequestDTO) {
        return islandResourceService.checkResourceAllocation(islandId, resourceAllocationRequestDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/")
    public Mono<IslandResource> prepareIslandResource(@Valid @RequestBody IslandResourceDTO initialIslandResourceDTO) {
        return islandResourceService.prepareIslandInitialResource(initialIslandResourceDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/assignResources/{islandid}")
    public Mono<IslandResource> assignResources(@Size(min = 1, message = "Id must be not empty") @PathVariable String islandId,
                                                @Valid @RequestBody ResourceAllocationRequestDTO resourceAllocationRequestDTO) {
        return islandResourceService.assignResources(islandId, resourceAllocationRequestDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/looting/{plunderedIslandId}/{raidingIslandId}")
    public Mono<Tuple2<IslandResource, IslandResource>> looting(@Size(min = 1, message = "Id must be not empty") @PathVariable String plunderedIslandId,
                                                                        @Size(min = 1, message = "Id must be not empty") @PathVariable String raidingIslandId,
                                                                        @Valid @RequestBody LootingResourcesRequestDTO lootingResourcesRequestDTO) {
        return islandResourceService.looting(plunderedIslandId, raidingIslandId, lootingResourcesRequestDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/mutualTrading/{senderIslandId}/{receiverIslandId}")
    public Mono<Tuple2<IslandResource, IslandResource>> mutualTrading(@Size(min = 1, message = "Id must be not empty") @PathVariable String island1Id,
                                                                      @Size(min = 1, message = "Id must be not empty") @PathVariable String island2Id,
                                                                      @Valid @RequestBody MutualTradingRequestDTO island1AndIsland2ResourcesDTO) {
        return islandResourceService.mutualTrading(island1Id, island2Id, island1AndIsland2ResourcesDTO.getIsland1MaterialsDTO(), island1AndIsland2ResourcesDTO.getIsland2MaterialsDTO());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/increaseWoodHourlyProduction/{islandId}")
    public Mono<Void> increaseWoodHourlyProduction(@Size(min = 1, message = "Id must be not empty") @PathVariable String islandId,
                                                   @Valid @RequestBody IncreaseResourceRequest newWoodHourlyProduction) {
        return islandResourceService.increaseWoodHourlyProduction(islandId, newWoodHourlyProduction.getNewHourlyResourceProduction());
    }

    @PutMapping("/")
    public Mono<IslandResource> update(@RequestBody IslandResource islandResource) {
        return islandResourceService.updateIslandResource(islandResource);
    }

    @PostMapping("/test")
    public Flux<IslandResource> addTest() {
        return islandResourceService.addTest();
    }

}
