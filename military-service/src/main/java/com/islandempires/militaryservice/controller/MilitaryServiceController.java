package com.islandempires.militaryservice.controller;

import com.islandempires.militaryservice.model.IslandMilitary;
import com.islandempires.militaryservice.service.WarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/military")
public class MilitaryServiceController {
    @Autowired
    private WarService warService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{serverId}/{islandId}")
    public IslandMilitary initializeIslandResource(@PathVariable String serverId, @PathVariable String  islandId) {
        return warService.initializeIslandMilitary(serverId, islandId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/war")
    public void war() {
        warService.test();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/end")
    public void warend() {
        warService.evaluateBattleVictory(Long.valueOf(1011));
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{islandId}")
    public IslandMilitary getIslandMilitaryWithId(@PathVariable String islandId) {
        return warService.getIslandMilitary(islandId);
    }
}
