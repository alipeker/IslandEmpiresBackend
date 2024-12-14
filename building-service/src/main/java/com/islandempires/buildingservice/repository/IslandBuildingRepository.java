package com.islandempires.buildingservice.repository;

import com.islandempires.buildingservice.model.IslandBuilding;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface IslandBuildingRepository extends ReactiveCrudRepository<IslandBuilding, String> {
    Flux<IslandBuilding> findByUserIdAndServerId(Long userId, String serverId);
}

