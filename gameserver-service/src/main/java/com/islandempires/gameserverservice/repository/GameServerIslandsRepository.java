package com.islandempires.gameserverservice.repository;

import com.islandempires.gameserverservice.model.GameServerIslands;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface GameServerIslandsRepository extends ReactiveCrudRepository<GameServerIslands, String> {
    @Query("{'islandId': ?0}")
    Mono<Void> deleteByIslandId(String islandId);

}
