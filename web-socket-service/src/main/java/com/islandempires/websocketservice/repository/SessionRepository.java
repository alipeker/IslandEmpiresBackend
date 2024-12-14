package com.islandempires.websocketservice.repository;

import com.islandempires.websocketservice.model.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SessionRepository extends MongoRepository<Session, String> {
    List<Session> findByServerIdAndUserId(String serverId, Long userId);
}
