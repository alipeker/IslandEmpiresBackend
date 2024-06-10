package com.islandempires.militaryservice.dto;

import com.islandempires.militaryservice.enums.SoldierTypeEnum;
import com.islandempires.militaryservice.model.resource.RawMaterialsAndPopulationCost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoldierBaseInfoDTO {
    private String id;
    private int attackPoint;
    private Map<SoldierTypeEnum, Integer> defensePoints;
    private RawMaterialsAndPopulationCost rawMaterialsAndPopulationCost;
    private int soldierCapacityOfShip;
    private int canonCapacityOfShip;
}
