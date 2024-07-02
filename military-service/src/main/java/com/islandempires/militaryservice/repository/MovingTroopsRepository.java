package com.islandempires.militaryservice.repository;

import com.islandempires.militaryservice.model.troopsAction.MovingTroops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface MovingTroopsRepository extends JpaRepository<MovingTroops, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM MovingTroops mt WHERE mt.isActive = false")
    void deleteInactiveMovingTroops();

    List<MovingTroops> findByIsActiveFalse();

    @Transactional
    void deleteByIsActiveFalse();
}
