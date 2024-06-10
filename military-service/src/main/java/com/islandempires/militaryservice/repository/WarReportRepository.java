package com.islandempires.militaryservice.repository;

import com.islandempires.militaryservice.model.war.WarReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarReportRepository extends JpaRepository<WarReport, Long> {
}
