package com.islandempires.clanservice.repository;

import com.islandempires.clanservice.model.Clan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClanRepository extends JpaRepository<Clan, Long> {
    @Query("SELECT c FROM Clan c WHERE c.server_id = :serverId AND c.id <> :clanId AND (c.name = :name OR c.shortName = :shortName)")
    Optional<Clan> findByServerIdAndNameOrShortNameAndIdNot(
            @Param("serverId") String serverId,
            @Param("clanId") Long clanId,
            @Param("name") String name,
            @Param("shortName") String shortName
    );
    @Transactional
    @Modifying
    @Query("DELETE FROM Clan c WHERE c.id = :clanId")
    void deleteClanById(Long clanId);

    List<Clan> findByServerId(String serverId);
}
