package com.islandempires.clanservice.repository;

import com.islandempires.clanservice.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRepository extends JpaRepository<Server, String> {
}
