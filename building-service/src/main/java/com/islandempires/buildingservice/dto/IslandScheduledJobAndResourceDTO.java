package com.islandempires.buildingservice.dto;

import com.islandempires.buildingservice.model.scheduled.BuildingScheduledTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IslandScheduledJobAndResourceDTO implements Serializable {
    private BuildingScheduledTask buildingScheduledTask;

    private IslandResourceDTO islandResourceDTO;
}
