package com.islandempires.militaryservice.controller;

import com.islandempires.militaryservice.dto.IslandResourceDTO;
import com.islandempires.militaryservice.dto.request.CreateSoldierRequest;
import com.islandempires.militaryservice.dto.request.WarMilitaryUnitRequest;
import com.islandempires.militaryservice.enums.MissionTypeEnum;
import com.islandempires.militaryservice.model.IslandMilitary;
import com.islandempires.militaryservice.model.war.WarReport;
import com.islandempires.militaryservice.service.SoldierService;
import com.islandempires.militaryservice.service.WarReportService;
import com.islandempires.militaryservice.service.WarService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("military/public")
@AllArgsConstructor
public class MilitaryServiceController {

    private final WarService warService;

    private final WarReportService warReportService;

    private final SoldierService soldierService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/war/{senderIslandMilitaryId}/{targetIslandMilitaryId}")
    public void war(@PathVariable String senderIslandMilitaryId, @PathVariable String targetIslandMilitaryId, @RequestBody WarMilitaryUnitRequest warMilitaryUnitRequest,
             @RequestAttribute("userId") Long userId) {
        warService.war(senderIslandMilitaryId, targetIslandMilitaryId, warMilitaryUnitRequest, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/pullBack/{warReportId}")
    public void pullBackTroop(@PathVariable Long troopId, @RequestAttribute("userId") Long userId) {
        warService.pullBackTroop(troopId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{islandId}")
    public IslandMilitary getIslandMilitaryWithId(@PathVariable String islandId, @RequestAttribute("userId") Long userId) {
        return warService.getIslandMilitary(islandId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/warReport/{warReportId}")
    public WarReport getWarReportId(@PathVariable Long warReportId, @RequestAttribute("userId") Long userId) {
        return warReportService.get(warReportId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/createSoldier/{islandId}")
    public IslandResourceDTO createSoldier(@PathVariable String  islandId, @RequestBody CreateSoldierRequest createSoldierRequest, @RequestAttribute("userId") Long userId) {
        return soldierService.createSoldier(islandId, userId, createSoldierRequest.getSoldierSubType(), createSoldierRequest.getSoldierCount());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/cancelSoldier/{soldierProductionId}")
    public IslandResourceDTO cancelSoldierProduction(@PathVariable Long soldierProductionId, @RequestAttribute("userId") Long userId) {
        return soldierService.cancelSoldierProduction(soldierProductionId, userId);
    }
}
