package com.islandempires.websocketservice.repository;

import com.islandempires.websocketservice.model.IslandResource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IslandResourceRepository extends MongoRepository<IslandResource, String> {
}
