package com.islandempires.resourcesservice.controller;

import com.islandempires.resourcesservice.filter.client.WhoAmIClient;
import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import com.islandempires.resourcesservice.dto.request.*;
import com.islandempires.resourcesservice.model.IslandResource;
import com.islandempires.resourcesservice.service.entityservice.IslandResourceInteractionService;
import com.islandempires.resourcesservice.service.entityservice.IslandResourceModificationService;
import com.islandempires.resourcesservice.service.entityservice.IslandResourceQueryService;
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
    private IslandResourceQueryService islandResourceQueryService;

    @Autowired
    private IslandResourceInteractionService islandResourceInteractionService;

    @Autowired
    private IslandResourceModificationService islandResourceModificationService;

    @Autowired
    private WhoAmIClient whoAmIClient;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<IslandResourceDTO> streamResources() {
        return islandResourceQueryService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{islandid}")
    public Mono<IslandResourceDTO> get(@PathVariable String islandid, @RequestAttribute("userId") Long userId) {
        return islandResourceQueryService.get(islandid, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/checkResourceAllocation/{islandId}")
    public Mono<Boolean> checkResourceAllocation(@Size(min = 1, message = "Id must be not empty") @PathVariable String islandId,
                                                 @Valid @RequestBody ResourceAllocationRequestDTO resourceAllocationRequestDTO,
                                                 @RequestAttribute("userId") Long userId) {
        return islandResourceInteractionService.validateResourceAllocationForIsland(islandId, resourceAllocationRequestDTO, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value= "/")
    public Mono<IslandResourceDTO> initializeIslandResource(@Valid @RequestBody IslandResourceDTO initialIslandResourceDTO,
                                                            @RequestAttribute("userId") Long userid) {
        return islandResourceInteractionService.initializeIslandResource(initialIslandResourceDTO, userid);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/assignResources/{islandId}")
    public Mono<IslandResourceDTO> assignResources(@Size(min = 1, message = "Id must be not empty") @PathVariable String islandId,
                                                @Valid @RequestBody ResourceAllocationRequestDTO resourceAllocationRequestDTO,
                                                   @RequestAttribute("userId") Long userId) {

        return islandResourceInteractionService.assignResources(islandId, resourceAllocationRequestDTO, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/looting/{plunderedIslandId}/{raidingIslandId}")
    public Mono<Tuple2<IslandResource, IslandResource>> looting(@Size(min = 1, message = "Id must be not empty") @PathVariable String plunderedIslandId,
                                                                        @Size(min = 1, message = "Id must be not empty") @PathVariable String raidingIslandId,
                                                                        @Valid @RequestBody LootingResourcesRequestDTO lootingResourcesRequestDTO) {
        return islandResourceInteractionService.looting(plunderedIslandId, raidingIslandId, lootingResourcesRequestDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/mutualTrading/{island1Id}/{island2Id}")
    public Mono<Tuple2<IslandResourceDTO, IslandResourceDTO>> mutualTrading(@Size(min = 1, message = "Id must be not empty") @PathVariable String island1Id,
                                                                      @Size(min = 1, message = "Id must be not empty") @PathVariable String island2Id,
                                                                      @Valid @RequestBody MutualTradingRequestDTO island1AndIsland2ResourcesDTO) {
        return islandResourceInteractionService.mutualTrading(island1Id, island2Id, island1AndIsland2ResourcesDTO.getIsland1RawMaterials(), island1AndIsland2ResourcesDTO.getIsland2RawMaterials());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/increaseOrDecreaseIslandResourceField/{islandId}")
    public Mono<IslandResourceDTO> increaseOrDecreaseIslandResourceField(@Size(min = 1, message = "Id must be not empty") @PathVariable String islandId,
                                                   @Valid @RequestBody IncreaseOrDecreaseIslandResourceFieldDTO increaseOrDecreaseIslandResourceFieldDTO) {
        return islandResourceModificationService.increaseOrDecreaseIslandResourceField(islandId, increaseOrDecreaseIslandResourceFieldDTO.getIslandResourceEnum(), increaseOrDecreaseIslandResourceFieldDTO.getValue());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/updateIslandResourceField/{islandId}")
    public Mono<IslandResourceDTO> updateIslandResourceField(@Size(min = 1, message = "Id must be not empty") @PathVariable String islandId,
                                                   @Valid @RequestBody UpdateIslandResourceFieldDTO updateIslandResourceFieldDTO) {
        return islandResourceModificationService.updateIslandResourceField(islandId, updateIslandResourceFieldDTO.getIslandResourceEnum(), updateIslandResourceFieldDTO.getValue());
    }

    /*
    @PutMapping("/")
    public Mono<IslandResourceDTO> update(@RequestBody IslandResource islandResource) {
        return islandResourceService.updateIslandResource(islandResource);
    }*/

    @PostMapping("/test")
    public Flux<IslandResource> addTest() {
        return islandResourceInteractionService.addTest();
    }


    @PostMapping("/test2")
    public Mono<Long> increaseIslandBuildingLvl(@RequestAttribute("userId") Long userId) {
        return Mono.just(userId);
    }
}
