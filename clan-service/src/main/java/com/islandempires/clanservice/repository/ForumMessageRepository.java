package com.islandempires.clanservice.repository;

import com.islandempires.clanservice.model.ForumMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumMessageRepository extends JpaRepository<ForumMessage, Long> {

}
