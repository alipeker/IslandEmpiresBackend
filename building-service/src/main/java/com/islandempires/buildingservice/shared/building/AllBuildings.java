package com.islandempires.buildingservice.shared.building;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.building.Building;
import com.islandempires.buildingservice.shared.buildingtype.BaseStructures;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class AllBuildings implements Serializable {
    private Academia academia;
    private Barrack barrack;
    private BrickWorks brickWorks;
    private CannonCamp cannonCamp;
    private ClayMine clayMine;
    private DairyFarm dairyFarm1;
    private DairyFarm dairyFarm2;
    private DefenceTower defenceTower;
    private Embassy embassy;
    private Fisher fisher;
    private Foundry foundry;
    private Gunsmith gunsmith;
    private Houses houses;
    private IronMine ironMine;
    private IslandHeadquarter islandHeadquarter;
    private Mill mill1;
    private Mill mill2;
    private RiffleBarrack riffleBarrack;
    private TimberCamp timberCamp1;
    private TimberCamp timberCamp2;
    private WatchTower watchTower;

    private WareHouse wareHouse;

    private FoodWareHouse foodWareHouse1;

    private FoodWareHouse foodWareHouse2;

    private CommercialPort commercialPort;

    private Shipyard shipyard;

    public AllBuildings() {

    }

    public BaseStructures findBuildingWithEnum(IslandBuildingEnum islandBuildingEnum) {
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
