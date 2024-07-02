package com.islandempires.gameserverservice.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.islandempires.gameserverservice.model.GameServerIslands;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface GameServerIslandsRepository extends ReactiveCrudRepository<GameServerIslands, String> {
    Mono<Boolean> existsByServerIdAndUserId(String serverId, Long userId);
}
