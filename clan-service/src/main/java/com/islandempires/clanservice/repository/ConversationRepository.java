package com.islandempires.clanservice.repository;

import com.islandempires.clanservice.model.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("SELECT c FROM Conversation c JOIN c.participants p WHERE p.id = :serverUserId ORDER BY c.lastMessageLocalDateTime DESC")
    Page<Conversation> findAllByParticipant(@Param("serverUserId") Long serverUserId, Pageable pageable);

    @Query("SELECT c FROM Conversation c " +
            "JOIN c.participants cp " +
            "WHERE cp.id IN :serverUserIds " +
            "GROUP BY c " +
            "HAVING COUNT(cp.id) = :participantCount AND COUNT(DISTINCT cp.id) = :participantCount")
    Optional<Conversation> findByParticipants(@Param("serverUserIds") List<Long> serverUserIds, @Param("participantCount") long participantCount);

}
