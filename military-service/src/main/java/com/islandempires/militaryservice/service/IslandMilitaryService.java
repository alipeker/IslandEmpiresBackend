package com.islandempires.militaryservice.service;

import com.islandempires.militaryservice.enums.SoldierSubTypeEnum;
import com.islandempires.militaryservice.model.IslandMilitary;
import com.islandempires.militaryservice.repository.IslandMilitaryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IslandMilitaryService {
    private final IslandMilitaryRepository islandMilitaryRepository;

    public void updateTimeReductionPercentageForInfantryman(String islandId, int percentage) {
        IslandMilitary islandMilitary = islandMilitaryRepository.findById(islandId)
                                        .orElseThrow();
        islandMilitary.setTimeReductionPercentageForInfantryman(percentage);
        islandMilitaryRepository.save(islandMilitary);
    }

    public void updateTimeReductionPercentageForRifle(String islandId, int percentage) {
        IslandMilitary islandMilitary = islandMilitaryRepository.findById(islandId)
                .orElseThrow();
        islandMilitary.setTimeReductionPercentageForRifle(percentage);
        islandMilitaryRepository.save(islandMilitary);
    }

    public void updateTimeReductionPercentageForCannon(String islandId, int percentage) {
        IslandMilitary islandMilitary = islandMilitaryRepository.findById(islandId)
                .orElseThrow();
        islandMilitary.setTimeReductionPercentageForCannon(percentage);
        islandMilitaryRepository.save(islandMilitary);
    }

    public void updateTimeReductionPercentageForShip(String islandId, int percentage) {
        IslandMilitary islandMilitary = islandMilitaryRepository.findById(islandId)
                .orElseThrow();
        islandMilitary.setTimeReductionPercentageForShip(percentage);
        islandMilitaryRepository.save(islandMilitary);
    }

    public void updateDefenseAttackMultiplier(String islandId, int percentage) {
        IslandMilitary islandMilitary = islandMilitaryRepository.findById(islandId)
                .orElseThrow();
        islandMilitary.setDefenceAndAttackMultiplier(percentage);
        islandMilitaryRepository.save(islandMilitary);
    }

    public void updateDefencePointPercentage(String islandId, double defencePointPercentage) {
        IslandMilitary islandMilitary = islandMilitaryRepository.findById(islandId)
                .orElseThrow();
        islandMilitary.setDefencePointPercentage(defencePointPercentage);
        islandMilitaryRepository.save(islandMilitary);
    }

    public void updateVisibilityRangeAndObservableCapacity(String islandId, int range, int capacity) {
        IslandMilitary islandMilitary = islandMilitaryRepository.findById(islandId)
                .orElseThrow();
        islandMilitary.setVisibilityRange(range);
        islandMilitary.setObservableCapacity(capacity);
        islandMilitaryRepository.save(islandMilitary);
    }

    public void updateMaxMissionaryCount(String islandId, int maxCount) {
        IslandMilitary islandMilitary = islandMilitaryRepository.findById(islandId)
                .orElseThrow();
        islandMilitary.setMaxMissionaryCount(maxCount);
        islandMilitaryRepository.save(islandMilitary);
    }

    public void addTotalCapturedIslandCount(String islandId) {
        IslandMilitary islandMilitary = islandMilitaryRepository.findById(islandId)
                .orElseThrow();
        int totalCapturedIsland = islandMilitary.getTotalCapturedIsles();
        totalCapturedIsland++;
        islandMilitary.setTotalCapturedIsles(totalCapturedIsland);
        islandMilitaryRepository.save(islandMilitary);
    }

    public void minusTotalCapturedIslandCount(String islandId) {
        IslandMilitary islandMilitary = islandMilitaryRepository.findById(islandId)
                .orElseThrow();
        int totalCapturedIsland = islandMilitary.getTotalCapturedIsles();
        totalCapturedIsland--;
        islandMilitary.setTotalCapturedIsles(totalCapturedIsland);
        islandMilitaryRepository.save(islandMilitary);
    }

    public void setSoldierTypeResearched(String islandId, SoldierSubTypeEnum soldierSubTypeEnum, boolean researched) {
        IslandMilitary islandMilitary = islandMilitaryRepository.findById(islandId)
                                            .orElseThrow();
        islandMilitary.setSoldierTypeResearched(soldierSubTypeEnum, researched);
        islandMilitaryRepository.save(islandMilitary);
    }

}
