package com.islandempires.buildingservice.model.scheduled;


import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.buildinglevelspec.BuildingLevel;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@Document("BuildingScheduledTask")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BuildingScheduledTask {
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

    public BuildingScheduledTask(String islandId, IslandBuildingEnum islandBuildingEnum, BuildingLevel initialLvl, BuildingLevel nextLvl, Duration constructionDuration) {
        this.islandId = islandId;
        this.islandBuildingEnum = islandBuildingEnum;
        this.initialLvl = initialLvl;
        this.nextLvl = nextLvl;
        this.constructionDuration = constructionDuration;
        this.startingDateTimestamp = System.currentTimeMillis();
        this.remainingTime = Duration.parse(constructionDuration.toString()).toMillis();
    }
}
