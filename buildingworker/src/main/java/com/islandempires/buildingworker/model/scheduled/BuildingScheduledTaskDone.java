package com.islandempires.buildingworker.model.scheduled;


import com.islandempires.buildingworker.enums.IslandBuildingEnum;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import com.islandempires.buildingworker.shared.resources.RawMaterialsAndPopulationCost;
import java.time.Duration;

@Document("BuildingScheduledTaskDone")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BuildingScheduledTaskDone {
    @Id
    private String id;

    private String serverId;

    private String islandId;

    private IslandBuildingEnum islandBuildingEnum;

    private RawMaterialsAndPopulationCost rawMaterialsAndPopulationCost;

    private int initialLvl;

    private int nextLvl;

    private Duration constructionDuration;

    private long remainingTime;

    private long startingDateTimestamp;

    private long lastCalculatedTimestamp;

}
