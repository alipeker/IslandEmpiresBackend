package com.islandempires.clanservice.repository;

import com.islandempires.clanservice.model.Clan;
import com.islandempires.clanservice.model.ClanToClanFinishWarRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ClanToClanFinishWarRequestRepository extends JpaRepository<ClanToClanFinishWarRequest, Long> {
    Optional<ClanToClanFinishWarRequest> findBySenderClanIdAndReceiverClanId(Long senderClanId, Long receiverClanId);

    Optional<ClanToClanFinishWarRequest> findBySenderClanAndReceiverClan(Clan senderClan, Clan receiverClan);

    @Transactional
    @Modifying
    @Query("DELETE FROM ClanToClanFinishWarRequest c WHERE c.senderClan.id = :clanId OR c.receiverClan.id = :clanId")
    void deleteByClanId(Long clanId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ClanToClanFinishWarRequest r WHERE " +
            "(r.senderClan = :clan1 AND r.receiverClan = :clan2) OR " +
            "(r.senderClan = :clan2 AND r.receiverClan = :clan1)")
    void deleteBySenderAndReceiverClans(Clan clan1, Clan clan2);
}
