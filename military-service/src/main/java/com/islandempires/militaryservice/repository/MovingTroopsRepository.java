package com.islandempires.militaryservice.repository;

import com.islandempires.militaryservice.model.troopsAction.MovingTroops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovingTroopsRepository extends JpaRepository<MovingTroops, Long> {

}
