package com.islandempires.clanservice.repository;

import com.islandempires.clanservice.model.ForumContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumContentRepository extends JpaRepository<ForumContent, Long> {
    @Query("SELECT fc FROM ForumContent fc WHERE fc.forum.clan.id = :clanId")
    Page<ForumContent> findByClanId(Long clanId, Pageable pageable);

    @Query("SELECT fc FROM ForumContent fc WHERE fc.forum_id = :forumId")
    Page<ForumContent> findByForumId(Long forumId, Pageable pageable);
}


