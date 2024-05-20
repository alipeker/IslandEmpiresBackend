package com.islandempires.buildingworker.repository;

import com.islandempires.buildingworker.shared.building.AllBuildingsServerProperties;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllBuildingsServerRepository extends MongoRepository<AllBuildingsServerProperties, String> {
}
