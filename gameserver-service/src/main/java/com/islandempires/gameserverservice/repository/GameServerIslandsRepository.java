package com.islandempires.gameserverservice.repository;

import com.islandempires.gameserverservice.model.GameServerIslands;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GameServerIslandsRepository extends ReactiveCrudRepository<GameServerIslands, String> {
}
