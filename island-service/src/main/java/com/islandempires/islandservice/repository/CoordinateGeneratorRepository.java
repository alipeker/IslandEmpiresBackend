package com.islandempires.islandservice.repository;

import com.islandempires.islandservice.model.CoordinateGenerator;
import com.islandempires.islandservice.model.Island;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CoordinateGeneratorRepository extends ReactiveCrudRepository<CoordinateGenerator, String> {
    Mono<CoordinateGenerator> findByServerId(String serverId);
}
