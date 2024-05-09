package com.islandempires.buildingservice.controller;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.IslandBuilding;
import com.islandempires.buildingservice.model.building.AllBuildings;
import com.islandempires.buildingservice.model.buildingtype.BaseStructures;
import com.islandempires.buildingservice.service.BuildingService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("building")
public class BuildingController {

  @Autowired
  private final BuildingService buildingService;

  private final ModelMapper modelMapper;


  @PostMapping("/{islandId}")
  public Mono<IslandBuilding> initializeIslandBuildings(@PathVariable String islandId, @RequestBody AllBuildings allBuildings,
                                                        @RequestHeader("userid") Long userid) {
    return buildingService.initializeIslandBuildings(islandId, allBuildings, userid);
  }

  @PostMapping("/increaselvl/{islandId}")
  public Mono<Void> increaseIslandBuildingLvl(@PathVariable String islandId, @RequestBody IslandBuildingEnum islandBuildingEnum,
                                                        @RequestHeader("userid") Long userid) {
    return buildingService.increaseIslandBuildingLvl(islandId, islandBuildingEnum, userid);
  }

  /*
  @GetMapping(value = "/me")
  public UserResponseDTO whoami(HttpServletRequest req) {
    return modelMapper.map(userService.whoami(req), UserResponseDTO.class);
  }
*/

}
