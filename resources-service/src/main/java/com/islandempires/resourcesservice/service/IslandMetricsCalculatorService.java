package com.islandempires.resourcesservice.service;

import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import org.springframework.stereotype.Service;

@Service
public class IslandMetricsCalculatorService {

    public IslandResourceDTO calculateIslandResourceFields(IslandResourceDTO islandResourceDTO) {
        islandResourceDTO = calculatePopulationLimit(islandResourceDTO);
        islandResourceDTO = calculateHappinessScore(islandResourceDTO);
        return islandResourceDTO;
    }

    public IslandResourceDTO calculatePopulationLimit(IslandResourceDTO islandResourceDTO) {
        islandResourceDTO.setPopulationLimit((int) (islandResourceDTO.getMeatHourlyProduction() * islandResourceDTO.getWheatFoodCoefficient()
                + islandResourceDTO.getFishHourlyProduction() * islandResourceDTO.getFishFoodCoefficient()
                + islandResourceDTO.getWheatHourlyProduction() * islandResourceDTO.getWheatFoodCoefficient()));
        return islandResourceDTO;
    }

    public IslandResourceDTO calculateHappinessScore(IslandResourceDTO islandResourceDTO) {
        double totalPopulation = islandResourceDTO.getPopulation() + islandResourceDTO.getTemporaryPopulation();
        double populationLimit = islandResourceDTO.getPopulationLimit();
        if(totalPopulation > populationLimit) {
            islandResourceDTO.setHappinessScore(populationLimit / totalPopulation);
        } else {
            islandResourceDTO.setHappinessScore((double)(islandResourceDTO.getAdditionalHappinessScore() + 1));
        }
        return islandResourceDTO;
    }
}
