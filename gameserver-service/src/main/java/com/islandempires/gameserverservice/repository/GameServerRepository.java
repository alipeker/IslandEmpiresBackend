package com.islandempires.gameserverservice.repository;

import com.islandempires.gameserverservice.model.GameServer;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameServerRepository extends ReactiveCrudRepository<GameServer, String> {
}
