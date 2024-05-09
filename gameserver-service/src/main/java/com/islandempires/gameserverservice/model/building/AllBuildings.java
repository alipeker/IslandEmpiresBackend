package com.islandempires.gameserverservice.model.building;

import com.islandempires.gameserverservice.enums.IslandBuildingEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class AllBuildings {
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

    public AllBuildings() {

    }
}
