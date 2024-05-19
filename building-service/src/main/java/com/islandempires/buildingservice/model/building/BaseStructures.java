package com.islandempires.buildingservice.model.building;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import lombok.Data;
import org.springframework.data.annotation.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseStructures implements Serializable {

    @Id
    protected IslandBuildingEnum islandBuildingEnum;

    private LocalDateTime createdAt;

    private int initialLvl = 0;

    BaseStructures() {
        this.createdAt = LocalDateTime.now();
    }

}
