package com.islandempires.militaryservice.controller;

import com.islandempires.militaryservice.enums.SoldierSubTypeEnum;
import com.islandempires.militaryservice.model.IslandMilitary;
import com.islandempires.militaryservice.model.war.WarReport;
import com.islandempires.militaryservice.service.IslandMilitaryService;
import com.islandempires.militaryservice.service.WarReportService;
import com.islandempires.militaryservice.service.WarService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("military/private")
@AllArgsConstructor
public class MilitaryServicePrivateController {

    private final WarService warService;

    private final WarReportService warReportService;

    private final IslandMilitaryService islandMilitaryService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{userId}/{serverId}/{islandId}")
    public IslandMilitary initializeIslandMilitary(@PathVariable Long userId, @PathVariable String serverId, @PathVariable String  islandId) {
        return warService.initializeIslandMilitary(serverId, islandId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{islandId}")
    public IslandMilitary getIslandMilitaryWithId(@PathVariable String islandId) {
        return warService.getIslandMilitary(islandId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getIslandsWithUserId/{userId}/{serverId}")
    public List<IslandMilitary> getIslandMilitariesWithUserId(@PathVariable Long userId, @PathVariable String serverId) {
        return warService.getIslandMilitaries(userId, serverId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/warReport/{warReportId}")
    public WarReport getIslandWarReportId(@PathVariable Long warReportId) {
        return warReportService.get(warReportId);
    }

    @PutMapping("/infantrymanTimeReduction/{islandId}/{percentage}")
    public void updateInfantrymanTimeReduction(@PathVariable String islandId, @PathVariable int percentage) {
        islandMilitaryService.updateTimeReductionPercentageForInfantryman(islandId, percentage);
    }

    @PutMapping("/rifleTimeReduction/{islandId}/{percentage}")
    public void updateRifleTimeReduction(@PathVariable String islandId, @PathVariable int percentage) {
        islandMilitaryService.updateTimeReductionPercentageForRifle(islandId, percentage);
    }

    @PutMapping("/cannonTimeReduction/{islandId}/{percentage}")
    public void updateCannonTimeReduction(@PathVariable String islandId, @PathVariable int percentage) {
        islandMilitaryService.updateTimeReductionPercentageForCannon(islandId, percentage);
    }

    @PutMapping("/shipTimeReduction/{islandId}/{percentage}")
    public void updateShipTimeReduction(@PathVariable String islandId, @PathVariable int percentage) {
        islandMilitaryService.updateTimeReductionPercentageForShip(islandId, percentage);
    }

    @PutMapping("/defenseAttackMultiplier/{islandId}/{percentage}")
    public void updateDefenseAttackMultiplier(@PathVariable String islandId, @PathVariable int percentage) {
        islandMilitaryService.updateDefenseAttackMultiplier(islandId, percentage);
    }

    @PutMapping("/defencePointPercentage/{islandId}/{percentage}")
    public void updateDefencePointPercentage(@PathVariable String islandId, @PathVariable double percentage) {
        islandMilitaryService.updateDefencePointPercentage(islandId, percentage);
    }

    @PutMapping("/visibilityRangeAndObservableCapacity/{islandId}/{range}/{capacity}")
    public void visibilityRangeAndObservableCapacity(@PathVariable String islandId, @PathVariable int range, @PathVariable int capacity) {
        islandMilitaryService.updateVisibilityRangeAndObservableCapacity(islandId, range, capacity);
    }

    @PutMapping("/maxMissionaryCount/{islandId}/{maxCount}")
    public void updateMaxMissionaryCount(@PathVariable String islandId, @PathVariable int maxCount) {
        islandMilitaryService.updateMaxMissionaryCount(islandId, maxCount);
    }

    @PutMapping("/addTotalCapturedIslandCount/{islandId}")
    public void addTotalCapturedIslandCount(@PathVariable String islandId) {
        islandMilitaryService.addTotalCapturedIslandCount(islandId);
    }

    @PutMapping("/minusTotalCapturedIslandCount/{islandId}")
    public void minusTotalCapturedIslandCount(@PathVariable String islandId) {
        islandMilitaryService.minusTotalCapturedIslandCount(islandId);
    }

    @PutMapping("/setSoldierTypeResearched/{islandId}/{soldierSubTypeEnum}/{researched}")
    public void setSoldierTypeResearched(@PathVariable String islandId,
                                         @PathVariable SoldierSubTypeEnum soldierSubTypeEnum,
                                         @PathVariable boolean researched) {
        islandMilitaryService.setSoldierTypeResearched(islandId, soldierSubTypeEnum, researched);
    }

}