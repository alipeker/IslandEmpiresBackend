package com.islandempires.buildingservice.model.building;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllBuildings implements Serializable {
    private Building academia;
    private Building barrack;
    private Building brickWorks;
    private Building cannonCamp;
    private Building clayMine;
    private Building dairyFarm1;
    private Building dairyFarm2;
    private Building defenceTower;
    private Building embassy;
    private Building fisher;
    private Building foundry;
    private Building gunsmith;
    private Building houses;
    private Building ironMine;
    private Building islandHeadquarter;
    private Building mill1;
    private Building mill2;
    private Building riffleBarrack;
    private Building timberCamp1;
    private Building timberCamp2;
    private Building watchTower;
    private Building wareHouse;
    private Building foodWareHouse1;
    private Building foodWareHouse2;
    private Building commercialPort;
    private Building shipyard;

    public Building findBuildingWithEnum(IslandBuildingEnum islandBuildingEnum) {
        return switch (islandBuildingEnum) {
            case ISLAND_HEADQUARTERS -> this.islandHeadquarter;
            case ACADEMIA -> this.academia;
            case BARRACK -> this.barrack;
            case RIFFLE_BARRACK -> this.riffleBarrack;
            case CANNON_CAMP -> this.cannonCamp;
            case GUNSMITH -> this.gunsmith;
            case TIMBER_CAMP1 -> this.timberCamp1;
            case TIMBER_CAMP2 -> this.timberCamp2;
            case CLAY_MINE -> this.clayMine;
            case BRICK_WORKS -> this.brickWorks;
            case IRON_MINE -> this.ironMine;
            case FOUNDRY -> this.foundry;
            case HOUSES -> this.houses;
            case DAIRY_FARM1 -> this.dairyFarm1;
            case DAIRY_FARM2 -> this.dairyFarm2;
            case FISHER -> this.fisher;
            case MILL1 -> this.mill1;
            case MILL2 -> this.mill2;
            case EMBASSY -> this.embassy;
            case WATCH_TOWER -> this.watchTower;
            case DEFENCE_TOWER -> this.defenceTower;
            case WAREHOUSE -> this.wareHouse;
            case FOOD_WAREHOUSE1 -> this.foodWareHouse1;
            case FOOD_WAREHOUSE2 -> this.foodWareHouse2;
            case COMMERCIAL_PORT -> this.commercialPort;
            case SHIPYARD -> this.shipyard;
            default -> null;
        };
    }
}

