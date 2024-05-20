package com.islandempires.buildingservice.repository;

import com.islandempires.buildingservice.model.building.Building;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseStructuresRepository extends ReactiveCrudRepository<Building, Long> {

}
