package com.islandempires.clanservice.repository;

import com.islandempires.clanservice.model.ClanToClanFriendRequest;
import com.islandempires.clanservice.model.Conversation;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ClanToClanFriendRequestRepository extends JpaRepository<ClanToClanFriendRequest, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM ClanToClanFriendRequest c WHERE c.senderClan.id = :clanId OR c.receiverClan.id = :clanId")
    void deleteByClanId(Long clanId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ClanToClanFriendRequest r WHERE (r.senderClan.id = :clanId1 AND r.receiverClan.id = :clanId2) " +
            "OR (r.receiverClan.id = :clanId1 AND r.senderClan.id = :clanId2)")
    void deleteBidirectionalClanRequest(@Param("clanId1") Long clanId1, @Param("clanId2") Long clanId2);



    void deleteBySenderClanIdAndReceiverClanId(Long clanId1, Long clanId2);

    @Query("SELECT r FROM ClanToClanFriendRequest r WHERE (r.senderClan.id = :clanId1 AND r.receiverClan.id = :clanId2) " +
            "OR (r.receiverClan.id = :clanId1 AND r.senderClan.id = :clanId2)")
    List<ClanToClanFriendRequest> findBidirectionalClanRequest(@Param("clanId1") Long clanId1, @Param("clanId2") Long clanId2);


}
