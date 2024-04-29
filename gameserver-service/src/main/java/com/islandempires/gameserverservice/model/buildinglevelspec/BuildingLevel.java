package com.islandempires.gameserverservice.model.buildinglevelspec;

import com.islandempires.gameserverservice.model.resources.RawMaterialsAndPopulationCost;
import lombok.*;

import java.io.Serializable;
import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BuildingLevel implements Serializable {

    private int level;

    private RawMaterialsAndPopulationCost rawMaterialsAndPopulationCost;

    private Duration constructionDuration;

}
