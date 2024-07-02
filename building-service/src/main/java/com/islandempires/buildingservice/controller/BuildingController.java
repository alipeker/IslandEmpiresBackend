package com.islandempires.buildingservice.controller;

import com.islandempires.buildingservice.dto.IslandResourceDTO;
import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.IslandBuilding;
import com.islandempires.buildingservice.service.BuildingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@AllArgsConstructor
@RequestMapping("building/public")
public class BuildingController {

    @Autowired
    private final BuildingService buildingService;

    @GetMapping("/{islandId}")
    public Mono<IslandBuilding> get(@PathVariable String islandId, @RequestAttribute("userId") Long userid) {
      return buildingService.get(islandId, userid);
    }

    @PatchMapping("/increaselvl/{islandId}")
    public Mono<IslandResourceDTO> increaseIslandBuildingLvl(@PathVariable String islandId, @RequestBody IslandBuildingEnum islandBuildingEnum,
                                                             @RequestAttribute("userId") Long userid) {
      return buildingService.increaseIslandBuildingLvl(islandId, islandBuildingEnum, userid);
    }


}
