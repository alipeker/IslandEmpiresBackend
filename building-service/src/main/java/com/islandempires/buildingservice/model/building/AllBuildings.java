package com.islandempires.buildingservice.model.building;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllBuildings implements Serializable {
    private static final long serialVersionUID = -664873475179751101L;
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


}

