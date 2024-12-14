package com.islandempires.clanservice.repository;

import com.islandempires.clanservice.model.Clan;
import com.islandempires.clanservice.model.ClanInviteRequest;
import com.islandempires.clanservice.model.ServerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ClanInviteRequestsRepository extends JpaRepository<ClanInviteRequest, Long> {
    Optional<ClanInviteRequest> findByClanAndServerUser(Clan clan, ServerUser inviteServerUser);

    @Query("DELETE FROM ClanInviteRequest cir WHERE cir.clan = :clan AND cir.serverUser = :serverUser")
    @Modifying
    @Transactional
    void deleteByClanAndServerUser(@Param("clan") Clan clan, @Param("serverUser") ServerUser serverUser);

    void deleteByClan(Clan clan);
}

