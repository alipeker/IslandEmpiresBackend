package com.islandempires.clanservice.repository;

import com.islandempires.clanservice.model.ServerUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServerUserRepository extends JpaRepository<ServerUser, Long> {
    @Query("SELECT su FROM ServerUser su WHERE su.user_id = :userId AND su.server_id = :serverId")
    Optional<ServerUser> findByUserIdAndServerId(@Param("userId") Long userId, @Param("serverId") String serverId);

    @EntityGraph(attributePaths = {"user", "clan", "server", "friendList", "blockList", "conversations", "clanPrivileges", "clanJoinRequests"})
    @Query("SELECT su FROM ServerUser su WHERE su.user_id = :userId AND su.server_id = :serverId")
    Optional<ServerUser> findByUserIdAndServerIdAll(@Param("userId") Long userId, @Param("serverId") String serverId);

    @Transactional
    @Modifying
    @Query("UPDATE ServerUser su SET su.clan = null WHERE su.clan.id = :clanId")
    void updateClanToNullByClanId(Long clanId);

    @Query("SELECT su FROM ServerUser su WHERE su.user_id = :userId AND su.clan.id = :clanId")
    Optional<ServerUser> findByUserIdAndClanId(@Param("userId") Long userId, @Param("clanId") Long clanId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ServerUser su WHERE su.server_id = :serverId AND su.user_id = :userId")
    void deleteByServerIdAndUserId(@Param("serverId") String serverId, @Param("userId") Long userId);

    @Query("SELECT su FROM ServerUser su WHERE su.server_id = :serverId")
    List<ServerUser> findByServerId(@Param("serverId") String serverId);

    @Transactional
    @Query("SELECT su FROM ServerUser su " +
            "LEFT JOIN FETCH su.clan c " +
            "LEFT JOIN FETCH c.users " +
            "LEFT JOIN FETCH su.blockList " +
            "WHERE su.server_id = :serverId")
    List<ServerUser> findByServerIdWithLazyFields(@Param("serverId") String serverId);

}
