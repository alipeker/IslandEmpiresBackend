package com.islandempires.buildingservice.controller;

import com.islandempires.buildingservice.dto.IslandResourceDTO;
import com.islandempires.buildingservice.dto.IslandScheduledJobAndResourceDTO;
import com.islandempires.buildingservice.dto.IslandBuildingAndScheduledJobDTO;
import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.enums.SoldierSubTypeEnum;
import com.islandempires.buildingservice.model.scheduled.BuildingScheduledTask;
import com.islandempires.buildingservice.service.BuildingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@AllArgsConstructor
@RequestMapping("building/public")
public class BuildingController {

    @Autowired
    private final BuildingService buildingService;

    @GetMapping("/{islandId}")
    public Mono<IslandBuildingAndScheduledJobDTO> get(@PathVariable String islandId, @RequestAttribute("userId") Long userid) {
      return buildingService.get(islandId, userid);
    }

    @GetMapping("/getScheduledJobs/{islandId}")
    public Flux<BuildingScheduledTask> getScheduledJobs(@PathVariable String islandId, @RequestAttribute("userId") Long userid) {
        return buildingService.getScheduledJobs(islandId, userid);
    }

    @PatchMapping("/increaselvl/{islandId}")
    public Mono<IslandScheduledJobAndResourceDTO> increaseIslandBuildingLvl(@PathVariable String islandId, @RequestBody IslandBuildingEnum islandBuildingEnum,
                                                                            @RequestAttribute("userId") Long userid) {
      return buildingService.increaseIslandBuildingLvl(islandId, islandBuildingEnum, userid);
    }

    @PostMapping("/research/{islandId}/{soldierSubTypeEnum}")
    public Mono<IslandResourceDTO> research(@PathVariable String islandId, @PathVariable SoldierSubTypeEnum soldierSubTypeEnum, @RequestAttribute("userId") Long userId) {
        return buildingService.research(islandId, soldierSubTypeEnum, userId);
    }
    

}
