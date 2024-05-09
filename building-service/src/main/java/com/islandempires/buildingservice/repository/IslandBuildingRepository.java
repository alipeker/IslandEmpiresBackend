package com.islandempires.buildingservice.repository;

import com.islandempires.buildingservice.model.IslandBuilding;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IslandBuildingRepository extends ReactiveCrudRepository<IslandBuilding, String> {
}

