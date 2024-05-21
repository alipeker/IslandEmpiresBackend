package com.islandempires.buildingworker.shared.strategypattern;


public class BuildingLevelUpContext {
    private BuildingLevelUpStrategy strategy;

    public BuildingLevelUpContext(BuildingLevelUpStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(BuildingLevelUpStrategy strategy) {
        this.strategy = strategy;
    }

    public void sendRequest(int nextLvl, String islandId) {
        strategy.islandBuildingLevelUpExecution(nextLvl, islandId);
    }
}
