package com.islandempires.buildingservice.dto;

import com.islandempires.buildingservice.model.building.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllBuildingsDTO {
    private BaseStructures academia;
    private BaseStructures barrack;
    private BaseStructures brickWorks;
    private BaseStructures cannonCamp;
    private BaseStructures clayMine;
    private BaseStructures dairyFarm;
    private BaseStructures defenceTower;
    private BaseStructures embassy;
    private BaseStructures fisher;
    private BaseStructures foundry;
    private BaseStructures gunsmith;
    private BaseStructures houses;
    private BaseStructures ironMine;
    private BaseStructures islandHeadquarter;
    private BaseStructures mill1;
    private BaseStructures mill2;
    private BaseStructures riffleBarrack;
    private BaseStructures timberCamp1;
    private BaseStructures timberCamp2;
    private BaseStructures watchTower;

    private BaseStructures wareHouse;

    private BaseStructures foodWareHouse1;

    private BaseStructures foodWareHouse2;
}
