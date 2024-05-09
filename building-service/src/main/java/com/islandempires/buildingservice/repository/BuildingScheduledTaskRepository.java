package com.islandempires.buildingservice.repository;

import com.islandempires.buildingservice.model.scheduled.BuildingScheduledTask;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingScheduledTaskRepository extends ReactiveCrudRepository<BuildingScheduledTask, String> {
}


