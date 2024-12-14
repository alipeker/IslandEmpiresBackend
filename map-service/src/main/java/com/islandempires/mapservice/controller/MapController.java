package com.islandempires.mapservice.controller;

import com.islandempires.mapservice.model.IslandCombined;
import com.islandempires.mapservice.service.ElasticService;
import com.islandempires.mapservice.service.MapService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("map/public")
@AllArgsConstructor
public class MapController {

    private final MapService mapService;

    private final ElasticService elasticService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{serverId}/{xStart}/{xEnd}/{yStart}/{yEnd}")
    private Flux<IslandCombined> getIslands(@PathVariable String serverId, @PathVariable int xStart, @PathVariable int xEnd,
                                            @PathVariable int yStart, @PathVariable int yEnd) {
        return mapService.searchIslandsByCoordinates(serverId, xStart, xEnd, yStart, yEnd);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/")
    private void initialize() {
        elasticService.saveAllIslandsToElasticsearch();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/test")
    private String test() {
        return "test";
    }
}
