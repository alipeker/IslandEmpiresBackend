package com.islandempires.resourcesservice.repository;

import com.islandempires.resourcesservice.model.IslandResource;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface IslandResourceRepository extends ReactiveCrudRepository<IslandResource, String> {
    Mono<IslandResource> findByIslandId(String islandId);

    Mono<Void> deleteByIslandId(String islandId);

    Flux<IslandResource> findByUserIdAndServerId(Long userId, String serverId);
}
