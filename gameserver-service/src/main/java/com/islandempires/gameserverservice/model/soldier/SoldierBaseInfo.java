package com.islandempires.gameserverservice.model.soldier;

import com.islandempires.gameserverservice.enums.SoldierSubTypeEnum;
import com.islandempires.gameserverservice.enums.SoldierTypeEnum;
import com.islandempires.gameserverservice.model.resources.RawMaterialsAndPopulationCost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.util.Map;


@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoldierBaseInfo {

    @Id
    private SoldierSubTypeEnum id;

    private int attackPoint;

    private Map<SoldierTypeEnum, Integer> defensePoints;

    private RawMaterialsAndPopulationCost rawMaterialsAndPopulationCost;

    private int soldierCapacityOfShip;

    private int canonCapacityOfShip;

    private int totalLootCapacity;

    private Duration timeToTraverseMapCell;

    private Duration productionDuration;
}
