package com.islandempires.buildingservice.dto;

import com.islandempires.buildingservice.model.IslandBuilding;
import com.islandempires.buildingservice.model.scheduled.BuildingScheduledTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IslandBuildingAndScheduledJobDTO implements Serializable {

    private IslandBuilding islandBuilding;

    private List<BuildingScheduledTask> buildingScheduledTaskList;


}
