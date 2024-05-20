package com.islandempires.buildingservice.shared.repository;

import com.islandempires.buildingservice.shared.building.AllBuildingsServerProperties;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllBuildingsServerRepository extends ReactiveCrudRepository<AllBuildingsServerProperties, String> {
}
