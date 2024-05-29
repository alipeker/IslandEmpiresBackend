package com.islandempires.militaryservice.repository;

import com.islandempires.militaryservice.model.soldierStats.MilitaryUnits;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MilitaryUnitsRepository extends ReactiveCrudRepository<MilitaryUnits, String> {
}
