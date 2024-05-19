package com.islandempires.buildingservice.controller;

import com.islandempires.buildingservice.dto.AllBuildingsDTO;
import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.IslandBuilding;
import com.islandempires.buildingservice.model.building.AllBuildings;
import com.islandempires.buildingservice.service.BuildingService;
import com.islandempires.buildingservice.service.client.IslandResourceWebClientNew;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@AllArgsConstructor
@RequestMapping("building")
public class BuildingController {

    @Autowired
    private final BuildingService buildingService;

    private final ModelMapper modelMapper;

    private final IslandResourceWebClientNew islandResourceWebClientNew;

    @GetMapping("/{islandId}")
    public Mono<IslandBuilding> get(@PathVariable String islandId, @RequestAttribute("userId") Long userid) {
      return buildingService.get(islandId, userid);
    }

    @PostMapping("/{islandId}")
    public Mono<IslandBuilding> initializeIslandBuildings(@PathVariable String islandId, @RequestBody AllBuildings allBuildings, @RequestAttribute("userId") Long userid) {
      return buildingService.initializeIslandBuildings(islandId, allBuildings, userid);
    }

    @PatchMapping("/increaselvl/{islandId}")
    public Mono<Void> increaseIslandBuildingLvl(@PathVariable String islandId, @RequestBody IslandBuildingEnum islandBuildingEnum,
                                                @RequestHeader("Authorization") String authorization, @RequestAttribute("userId") Long userid) {
      return buildingService.increaseIslandBuildingLvl(islandId, islandBuildingEnum, authorization, userid);
    }

    @PostMapping("/test")
    public Mono<Long> increaseIslandBuildingLvl(@RequestHeader("Authorization") String authorization) {
      return islandResourceWebClientNew.whoami(authorization);
    }

    /*
    @GetMapping(value = "/me")
    public UserResponseDTO whoami(HttpServletRequest req) {
      return modelMapper.map(userService.whoami(req), UserResponseDTO.class);
    }
  */

}
