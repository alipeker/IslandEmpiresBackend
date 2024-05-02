package com.islandempires.islandservice.repository;

import com.islandempires.islandservice.model.Island;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IslandRepository extends ReactiveCrudRepository<Island, String> {

}
