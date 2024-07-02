package com.islandempires.militaryservice.repository;

import com.islandempires.militaryservice.model.war.WarReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarReportRepository extends JpaRepository<WarReport, Long> {
    List<WarReport> findByAttackerIslandMilitary_IslandId(String islandId);
    List<WarReport> findByDefenderIslandMilitary_IslandId(String islandId);
}
