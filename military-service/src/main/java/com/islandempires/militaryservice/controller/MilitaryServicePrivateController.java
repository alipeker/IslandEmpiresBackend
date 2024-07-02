package com.islandempires.militaryservice.controller;

import com.islandempires.militaryservice.model.IslandMilitary;
import com.islandempires.militaryservice.model.war.WarReport;
import com.islandempires.militaryservice.service.WarReportService;
import com.islandempires.militaryservice.service.WarService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("military/private")
@AllArgsConstructor
public class MilitaryServicePrivateController {

    private final WarService warService;

    private final WarReportService warReportService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{userId}/{serverId}/{islandId}")
    public IslandMilitary initializeIslandResource(@PathVariable Long userId, @PathVariable String serverId, @PathVariable String  islandId) {
        return warService.initializeIslandMilitary(serverId, islandId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{islandId}")
    public IslandMilitary getIslandMilitaryWithId(@PathVariable String islandId) {
        return warService.getIslandMilitary(islandId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/warReport/{warReportId}")
    public WarReport getIslandWarReportId(@PathVariable Long warReportId) {
        return warReportService.get(warReportId);
    }


}