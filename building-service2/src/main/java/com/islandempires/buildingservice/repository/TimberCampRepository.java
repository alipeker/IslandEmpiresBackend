package com.islandempires.buildingservice.repository;

import com.islandempires.buildingservice.model.TimberCamp;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimerCampRepository extends ReactiveMongoRepository<TimberCamp, Integer> {

}
