package com.islandempires.gameserverservice.model.buildinglevelspec;

import com.islandempires.gameserverservice.model.resources.RawMaterialsAndPopulationCost;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildingLevel implements Serializable {

    @Id
    private int level;

    private RawMaterialsAndPopulationCost rawMaterialsAndPopulationCost;

    private Duration constructionDuration;

}
