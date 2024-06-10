package com.islandempires.militaryservice.repository;

import com.islandempires.militaryservice.model.soldier.SoldierBaseInfo;
import com.islandempires.militaryservice.model.troopsAction.MovingTroops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoldierBaseInfoRepository extends JpaRepository<SoldierBaseInfo, Long> {

}


