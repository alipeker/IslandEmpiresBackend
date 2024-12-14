package com.islandempires.buildingservice.controller;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.IslandBuilding;
import com.islandempires.buildingservice.model.building.AllBuildings;
import com.islandempires.buildingservice.service.BuildingPrivateService;
import com.islandempires.buildingservice.service.client.GatewayWebClientService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import com.islandempires.buildingservice.dto.IslandBuildingAndScheduledJobDTO;

@RestController
@AllArgsConstructor
@RequestMapping("building/private")
public class BuildingPrivateController {

    @Autowired
    private final BuildingPrivateService buildingService;

    private final ModelMapper modelMapper;

    private final GatewayWebClientService gatewayWebClientService;

    @GetMapping("/{islandId}")
    public Mono<IslandBuilding> get(@PathVariable String islandId) {
        return buildingService.get(islandId);
    }

    @GetMapping("/getUserBuildings/{userId}/{serverId}")
    public Flux<IslandBuildingAndScheduledJobDTO> get(@PathVariable Long userId, @PathVariable String serverId) {
      return buildingService.get(userId, serverId);
    }

    // @GetMapping("/getUserBuildings/{userId}/{serverId}")
    // public Flux<IslandBuilding> getUserIdAndServerId(@PathVariable Long userId, @PathVariable String serverId) {
    //     return buildingService.getUserIdAndServerId(userId, serverId);
    // }

    @PostMapping("/{serverId}/{islandId}/{userid}")
    public Mono<IslandBuilding> initializeIslandBuildings(@PathVariable String serverId, @PathVariable String islandId,
                                                          @PathVariable Long userid, @RequestBody AllBuildings allBuildings) {
        return buildingService.initializeIslandBuildings(serverId, islandId, allBuildings, userid);
    }

    @PatchMapping("/increaseIslandBuildingLvlDone/{islandId}/{newLvl}")
    public Mono<Void> increaseIslandBuildingLvlDone(@PathVariable String islandId, @PathVariable int newLvl, @RequestBody IslandBuildingEnum islandBuildingEnum) {
        return buildingService.increaseIslandBuildingLvlDone(islandId, islandBuildingEnum, newLvl);
    }

    @PatchMapping("/increaseIslandBuildingLvlDoneRollback/{islandId}/{newLvl}")
    public Mono<Void> increaseIslandBuildingLvlDoneRollback(@PathVariable String islandId, @PathVariable int newLvl, @RequestBody IslandBuildingEnum islandBuildingEnum) {
        return buildingService.increaseIslandBuildingLvlDone(islandId, islandBuildingEnum, newLvl);
    }


}

