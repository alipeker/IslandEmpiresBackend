package com.islandempires.resourcesservice.repository;

import com.islandempires.resourcesservice.model.IslandResource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IslandResourceRepository extends ReactiveCrudRepository<IslandResource, String> {

}
