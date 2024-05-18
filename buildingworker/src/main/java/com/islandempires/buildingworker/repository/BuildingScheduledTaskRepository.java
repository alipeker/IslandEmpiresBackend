package com.islandempires.buildingworker.repository;

import com.islandempires.buildingworker.model.scheduled.BuildingScheduledTaskDone;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingScheduledTaskRepository extends MongoRepository<BuildingScheduledTaskDone, String> {
}


