package com.islandempires.clanservice.repository;

import com.islandempires.clanservice.model.Clan;
import com.islandempires.clanservice.model.ClanInviteRequest;
import com.islandempires.clanservice.model.ClanJoinRequests;
import com.islandempires.clanservice.model.ServerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ClanJoinRequestsRepository extends JpaRepository<ClanJoinRequests, Long> {
    Optional<ClanJoinRequests> findByClanAndServerUser(Clan clan, ServerUser inviteServerUser);

    @Query("DELETE FROM ClanJoinRequests cjr WHERE cjr.clan = :clan AND cjr.serverUser = :serverUser")
    @Modifying
    @Transactional
    void deleteByClanAndServerUser(@Param("clan") Clan clan, @Param("serverUser") ServerUser serverUser);

    @Query("SELECT CASE WHEN COUNT(cjr) > 0 THEN true ELSE false END " +
            "FROM ClanJoinRequests cjr " +
            "WHERE cjr.clan.id = :clanId AND cjr.serverUser.id = :serverUserId")
    boolean existsByClanIdAndServerUserId(@Param("clanId") Long clanId, @Param("serverUserId") Long serverUserId);
}
