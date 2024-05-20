package com.islandempires.buildingworker.shared.strategypattern;

import com.islandempires.buildingworker.model.strategy.BuildingStrategy;

public class BuildingLevelUpContext {
    private BuildingStrategy strategy;

    public BuildingLevelUpContext(BuildingStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(BuildingStrategy strategy) {
        this.strategy = strategy;
    }

    public void sendRequest() {
        strategy.executeBuildingLogic();
    }
}
