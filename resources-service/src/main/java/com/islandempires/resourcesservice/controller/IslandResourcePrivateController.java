package com.islandempires.resourcesservice.controller;


import com.islandempires.resourcesservice.filter.client.WhoAmIClient;
import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import com.islandempires.resourcesservice.dto.request.*;
import com.islandempires.resourcesservice.model.IslandResource;
import com.islandempires.resourcesservice.service.privates.IslandResourceInteractionPrivateService;
import com.islandempires.resourcesservice.service.privates.IslandResourceModificationPrivateService;
import com.islandempires.resourcesservice.service.privates.IslandResourceQueryPrivateService;
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
@RequestMapping("resource/private")
public class IslandResourcePrivateController {

    @Autowired
    private IslandResourceQueryPrivateService islandResourceQueryService;

    @Autowired
    private IslandResourceInteractionPrivateService islandResourceInteractionService;

    @Autowired
    private IslandResourceModificationPrivateService islandResourceModificationService;

    @Autowired
    private WhoAmIClient whoAmIClient;

    @GetMapping(value = "/getAll")
    public Flux<IslandResourceDTO> streamResources() {
        return islandResourceQueryService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{islandid}")
    public Mono<IslandResourceDTO> get(@PathVariable String islandid) {
        return islandResourceQueryService.get(islandid);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getByUserId/{userId}/{serverId}")
    public Flux<IslandResourceDTO> getByUserId(@PathVariable Long userId, @PathVariable String serverId) {
        return islandResourceQueryService.getByUserId(userId, serverId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/checkResourceAllocation/{islandId}")
    public Mono<Boolean> checkResourceAllocation(@Size(min = 1, message = "Id must be not empty") @PathVariable String islandId,
                                                 @Valid @RequestBody ResourceAllocationRequestDTO resourceAllocationRequestDTO) {
        return islandResourceInteractionService.validateResourceAllocationForIsland(islandId, resourceAllocationRequestDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value= "/{serverId}/{islandId}/{userId}")
    public Mono<IslandResourceDTO> initializeIslandResource(@PathVariable String serverId,
                                                            @PathVariable String islandId,
                                                            @PathVariable Long userId,
                                                            @RequestBody IslandResourceDTO initialIslandResourceDTO) {
        return islandResourceInteractionService.initializeIslandResource(serverId, islandId, initialIslandResourceDTO, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/assignResources/{islandId}")
    public Mono<IslandResourceDTO> assignResources(@Size(min = 1, message = "Id must be not empty") @PathVariable String islandId,
                                                   @RequestBody ResourceAllocationRequestDTO resourceAllocationRequestDTO) {

        return islandResourceInteractionService.assignResources(islandId, resourceAllocationRequestDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/refundResources/{islandId}")
    public Mono<IslandResourceDTO> refundResources(@Size(min = 1, message = "Id must be not empty") @PathVariable String islandId,
                                                   @RequestBody ResourceAllocationRequestDTO resourceAllocationRequestDTO) {

        return islandResourceInteractionService.refundResources(islandId, resourceAllocationRequestDTO);
    }

// 1.000.000 $ looting from ali peker
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

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/updateIslandTrading/{islandId}")
    public Mono<IslandResourceDTO> updateIslandTrading(@PathVariable String islandId, @RequestBody UpdateIslandTradingDTO updateIslandTradingDTO) {
        return islandResourceModificationService.updateIslandTrading(islandId, updateIslandTradingDTO.getTotalShipNumber(), updateIslandTradingDTO.getShipCapacity(), updateIslandTradingDTO.getTimeReductionPercentage());
    }

    // get population of user serverid islands
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getPopulations/{serverId}/{userId}")
    public Flux<IslandResourceDTO> getPopulations(@PathVariable String serverId, @PathVariable Long userId) {
        return islandResourceQueryService.getPopulations(serverId, userId);
    }

}

