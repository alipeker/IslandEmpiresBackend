package com.islandempires.resourcesservice.service;

import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import com.islandempires.resourcesservice.model.IslandResource;
import org.springframework.stereotype.Service;

@Service
public class IslandMetricsCalculatorService {

    public IslandResource calculateIslandResourceFields(IslandResource islandResource) {
        islandResource = calculatePopulationLimit(islandResource);
        islandResource = calculateHappinessScore(islandResource);
        return islandResource;
    }

    public IslandResource calculatePopulationLimit(IslandResource islandResource) {
        islandResource.setPopulationLimit((int) (islandResource.getMeatHourlyProduction() * islandResource.getWheatFoodCoefficient()
                + islandResource.getFishHourlyProduction() * islandResource.getFishFoodCoefficient()
                + islandResource.getWheatHourlyProduction() * islandResource.getWheatFoodCoefficient()));
        return islandResource;
    }

    public IslandResource calculateHappinessScore(IslandResource islandResource) {
        double totalPopulation = islandResource.getPopulation() + islandResource.getTemporaryPopulation();
        double populationLimit = islandResource.getPopulationLimit();
        if(totalPopulation > populationLimit) {
            islandResource.setHappinessScore(populationLimit / totalPopulation);
        } else {
            islandResource.setHappinessScore((double)(islandResource.getAdditionalHappinessScore() + 1));
        }
        return islandResource;
    }
}
