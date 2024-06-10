package com.islandempires.gameserverservice.repository;

import com.islandempires.gameserverservice.model.GameServer;
import com.islandempires.gameserverservice.model.GameServerIslandResource;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameServerIslandResourceRepository extends ReactiveCrudRepository<GameServerIslandResource, String> {
}
