package com.islandempires.militaryservice.model.soldierStats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawMaterialsAndPopulationCost {

    private int wood;

    private int clay;

    private int iron;

    private int population;

}
