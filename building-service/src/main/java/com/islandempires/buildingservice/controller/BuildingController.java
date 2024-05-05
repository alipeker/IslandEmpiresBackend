package com.islandempires.buildingservice.controller;

import com.islandempires.buildingservice.dto.AllBuildingsDTO;
import com.islandempires.buildingservice.model.IslandBuilding;
import com.islandempires.buildingservice.model.building.AllBuildings;
import com.islandempires.buildingservice.service.BuildingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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


  @PostMapping("/{islandId}")
  public Mono<IslandBuilding> initializeIslandBuildings(@PathVariable String islandId, @RequestBody AllBuildings allBuildings) {
    return buildingService.initializeIslandBuildings(islandId, allBuildings);
  }


  /*
  @GetMapping(value = "/me")
  public UserResponseDTO whoami(HttpServletRequest req) {
    return modelMapper.map(userService.whoami(req), UserResponseDTO.class);
  }
*/

}
