package com.islandempires.resourcesservice.service;

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
        islandResource.setPopulationLimit((int) (((islandResource.getWheatHourlyProduction() * islandResource.getWheatFoodCoefficient())
                + (islandResource.getFishHourlyProduction() * islandResource.getFishFoodCoefficient())
                + (islandResource.getMeatHourlyProduction() * islandResource.getMeatFoodCoefficient()))));
        return islandResource;
    }

    public IslandResource calculateHappinessScore(IslandResource islandResource) {
        double totalPopulation = islandResource.getPopulation();
        double populationLimit = islandResource.getPopulationLimit();
        if(totalPopulation > populationLimit) {
            islandResource.setHappinessScore(islandResource.getAdditionalHappinessScore() + (populationLimit / totalPopulation));
        } else {
            islandResource.setHappinessScore(islandResource.getAdditionalHappinessScore() + 1);
        }
        return islandResource;
    }
}
