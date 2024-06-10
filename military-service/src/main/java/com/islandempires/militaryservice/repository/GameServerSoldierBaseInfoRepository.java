package com.islandempires.militaryservice.repository;

import com.islandempires.militaryservice.model.GameServerSoldier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameServerSoldierBaseInfoRepository extends JpaRepository<GameServerSoldier, String> {

}
