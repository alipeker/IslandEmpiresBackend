package com.islandempires.resourceworker.repository;

import com.islandempires.resourceworker.mongodb.IslandResource;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface IslandResourceRepository extends MongoRepository<IslandResource, String> {
}
