package com.islandempires.clanservice.repository;

import com.islandempires.clanservice.model.Clan;
import com.islandempires.clanservice.model.ReserveIsland;
import com.islandempires.clanservice.model.ServerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReserveIslandRepository extends JpaRepository<ReserveIsland, Long> {
    void deleteByClan(Clan clan);
}

