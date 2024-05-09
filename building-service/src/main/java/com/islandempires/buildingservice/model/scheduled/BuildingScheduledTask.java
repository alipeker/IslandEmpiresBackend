package com.islandempires.buildingservice.model.scheduled;


import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.buildinglevelspec.BuildingLevel;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@Document("BuildingScheduledTask")
@AllArgsConstructor
@Data
public class BuildingScheduledTask {
    @Id
    private String id;

    private String islandId;

    private IslandBuildingEnum islandBuildingEnum;

    private BuildingLevel initialLvl;

    private BuildingLevel nextLvl;

    private Duration constructionDuration;

    private Duration remainingTime;

    private LocalDateTime startingDate;

    public BuildingScheduledTask(String islandId, IslandBuildingEnum islandBuildingEnum, BuildingLevel initialLvl, BuildingLevel nextLvl, Duration constructionDuration) {
        this.islandId = islandId;
        this.islandBuildingEnum = islandBuildingEnum;
        this.initialLvl = initialLvl;
        this.nextLvl = nextLvl;
        this.constructionDuration = constructionDuration;
        this.startingDate = LocalDateTime.now();
    }
}
