package com.islandempires.militaryservice.repository;

import com.islandempires.militaryservice.model.IslandMilitary;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IslandMilitaryRepository extends ReactiveCrudRepository<IslandMilitary, String> {
}
