package com.islandempires.buildingservice.repository;

import com.islandempires.buildingservice.model.IslandBuilding;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IslandBuildingRepository extends ReactiveCrudRepository<IslandBuilding, String> {
    Mono<Void> deleteByIslandId(String islandId);
}

