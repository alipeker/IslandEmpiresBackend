package com.islandempires.militaryservice.repository;

import com.islandempires.militaryservice.model.troopsAction.Troops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TroopsRepository extends JpaRepository<Troops, Long> {
}
