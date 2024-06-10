package com.islandempires.militaryservice.repository;

import com.islandempires.militaryservice.model.IslandMilitary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface IslandMilitaryRepository extends JpaRepository<IslandMilitary, String> {
    @Query("SELECT im FROM IslandMilitary im " +
            "LEFT JOIN FETCH im.stationaryTroops " +
            "WHERE im.islandId = :islandId")
    IslandMilitary findByIdWithLeftJoins(@Param("islandId") String islandId);
}
