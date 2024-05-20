package com.islandempires.buildingservice.shared.buildinglevelspec;

import com.islandempires.buildingservice.shared.resources.RawMaterialsAndPopulationCost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
