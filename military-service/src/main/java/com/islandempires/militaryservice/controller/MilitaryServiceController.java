package com.islandempires.militaryservice.controller;

import com.islandempires.militaryservice.model.IslandMilitary;
import com.islandempires.militaryservice.model.war.WarReport;
import com.islandempires.militaryservice.service.WarReportService;
import com.islandempires.militaryservice.service.WarService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/military")
@AllArgsConstructor
public class MilitaryServiceController {

    private final WarService warService;

    private final WarReportService warReportService;

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
        warService.evaluateBattleVictory(Long.valueOf(2009));
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{islandId}")
    public IslandMilitary getIslandMilitaryWithId(@PathVariable String islandId) {
        return warService.getIslandMilitary(islandId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/warReport/{warReportId}")
    public WarReport getIslandMilitaryWithId(@PathVariable Long warReportId) {
        return warReportService.get(warReportId);
    }
}
