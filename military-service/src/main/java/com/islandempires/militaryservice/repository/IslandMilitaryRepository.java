package com.islandempires.militaryservice.repository;

import com.islandempires.militaryservice.model.IslandMilitary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface IslandMilitaryRepository extends JpaRepository<IslandMilitary, String> {
    @Query("SELECT im FROM IslandMilitary im " +
            "LEFT JOIN FETCH im.stationaryTroops " +
            "WHERE im.islandId = :islandId")
    IslandMilitary findByIdWithLeftJoins(@Param("islandId") String islandId);

    List<IslandMilitary> findAllByIslandIdIn(List<String> islandIdList);

    List<IslandMilitary> findByServerIdAndUserId(String serverId, Long userId);
}
