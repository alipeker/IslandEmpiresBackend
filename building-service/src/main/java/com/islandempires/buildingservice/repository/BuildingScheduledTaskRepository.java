package com.islandempires.buildingservice.repository;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.scheduled.BuildingScheduledTask;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BuildingScheduledTaskRepository extends ReactiveCrudRepository<BuildingScheduledTask, String> {
    Flux<BuildingScheduledTask> findByIslandBuildingEnumAndIslandId(IslandBuildingEnum islandBuildingEnum, String islandId);
}


