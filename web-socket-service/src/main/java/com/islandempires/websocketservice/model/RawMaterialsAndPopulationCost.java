package com.islandempires.websocketservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawMaterialsAndPopulationCost implements Serializable {
    protected Long id;

    private int wood;

    private int clay;

    private int iron;

    private int population;

    private SoldierProduction soldierProduction;
}
