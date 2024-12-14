package com.islandempires.buildingservice.shared.repository;

import com.islandempires.buildingservice.shared.BuildingServerProperties;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BuildingsServerPropertiesRepository extends ReactiveCrudRepository<BuildingServerProperties, String> {
    Mono<BuildingServerProperties> findByServerId(String serverId);
}
