package com.islandempires.militaryservice.repository;

import com.islandempires.militaryservice.model.production.SoldierProduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SoldierProductionRepository extends JpaRepository<SoldierProduction, Long> {
    @Transactional
    void deleteByIsActiveFalse();

    @Modifying
    @Transactional
    @Query("DELETE FROM SoldierProduction sp WHERE sp.soldierCount = 0")
    void deleteInactive();
}

