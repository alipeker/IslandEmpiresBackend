package com.islandempires.websocketservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoldierProduction implements Serializable {
    private Long id;

    private String islandMilitaryId;

    private SoldierSubTypeEnum soldierSubType;

    private int soldierCount;

    private RawMaterialsAndPopulationCost rawMaterialsAndPopulationCost;

    private LocalDateTime time;

    private Duration productionDuration;

    private Boolean isActive;
}
