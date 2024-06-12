package com.islandempires.militaryservice.repository;

import com.islandempires.militaryservice.model.troopsAction.MovingTroops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovingTroopsRepository extends JpaRepository<MovingTroops, Long> {
    @Modifying
    @Query("DELETE FROM MovingTroops mt WHERE mt.id = :id")
    void deleteByIdQuery(Long id);
}
