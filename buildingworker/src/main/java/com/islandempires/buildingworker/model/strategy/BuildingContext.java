package com.islandempires.buildingworker.model.strategy;

import com.islandempires.buildingworker.enums.IslandBuildingEnum;
import com.islandempires.buildingworker.shared.building.TimberCamp;

public class BuildingContext {

    private BuildingStrategy strategy;

    public BuildingContext(BuildingStrategy strategy, IslandBuildingEnum islandBuildingEnum) {
        this.strategy = strategy;

        /*
        switch (islandBuildingEnum) {
            case TIMBER_CAMP1:
                this.strategy = (TimberCamp) this.strategy;
                this.strategy.executeBuildingLogic();
                break;
        }*/

    }

    public void setStrategy(BuildingStrategy strategy) {
        this.strategy = strategy;
    }

    public void executeBuildingLogic() {
        strategy.executeBuildingLogic();
    }
}

