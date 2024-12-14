package com.islandempires.buildingservice.model.scheduled;


import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.shared.resources.RawMaterialsAndPopulationCost;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;

@Document("BuildingScheduledTask")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BuildingScheduledTask {
    @Id
    private String id;

    private Long userId;

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

    private boolean isException = false;

    private long actualStartTimeStamp;

    public BuildingScheduledTask(String serverId, String islandId, IslandBuildingEnum islandBuildingEnum, int initialLvl,
                                 int nextLvl, Duration constructionDuration, RawMaterialsAndPopulationCost rawMaterialsAndPopulationCost,
                                 Long userId) {
        this.serverId = serverId;
        this.islandId = islandId;
        this.islandBuildingEnum = islandBuildingEnum;
        this.initialLvl = initialLvl;
        this.nextLvl = nextLvl;
        this.constructionDuration = constructionDuration;
        this.startingDateTimestamp = System.currentTimeMillis();
        this.remainingTime = Duration.parse(constructionDuration.toString()).toMillis();
        this.rawMaterialsAndPopulationCost = rawMaterialsAndPopulationCost;
        this.userId = userId;
    }

    public boolean getException() {
        return isException;
    }
}
