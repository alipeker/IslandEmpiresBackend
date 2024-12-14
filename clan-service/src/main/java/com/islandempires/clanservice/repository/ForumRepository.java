package com.islandempires.clanservice.repository;

import com.islandempires.clanservice.model.Clan;
import com.islandempires.clanservice.model.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForumRepository extends JpaRepository<Forum, Long> {
    Optional<Forum> findByClanId(Long clanId);

    void deleteByClan(Clan clan);
}
