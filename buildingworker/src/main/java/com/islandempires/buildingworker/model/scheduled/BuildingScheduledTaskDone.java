package com.islandempires.buildingworker.model.scheduled;


import com.islandempires.buildingworker.enums.IslandBuildingEnum;
import com.islandempires.buildingworker.model.buildinglevelspec.BuildingLevel;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;

@Document("BuildingScheduledTaskDone")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BuildingScheduledTaskDone {
    @Id
    private String id;

    private String islandId;

    private IslandBuildingEnum islandBuildingEnum;

    private BuildingLevel initialLvl;

    private BuildingLevel nextLvl;

    private Duration constructionDuration;

    private long remainingTime;

    private long startingDateTimestamp;

    private long lastCalculatedTimestamp;

    public BuildingScheduledTaskDone(String islandId, IslandBuildingEnum islandBuildingEnum, BuildingLevel initialLvl, BuildingLevel nextLvl, Duration constructionDuration) {
        this.islandId = islandId;
        this.islandBuildingEnum = islandBuildingEnum;
        this.initialLvl = initialLvl;
        this.nextLvl = nextLvl;
        this.constructionDuration = constructionDuration;
        this.startingDateTimestamp = System.currentTimeMillis();
        this.remainingTime = Duration.parse(constructionDuration.toString()).toMillis();
    }
}
