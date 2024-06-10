package com.islandempires.militaryservice.repository;

import com.islandempires.militaryservice.model.troopsAction.StationaryTroops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationaryTroopsRepository extends JpaRepository<StationaryTroops, Long> {
}