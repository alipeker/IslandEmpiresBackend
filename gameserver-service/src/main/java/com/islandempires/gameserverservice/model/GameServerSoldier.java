package com.islandempires.gameserverservice.model;

import com.islandempires.gameserverservice.model.soldier.SoldierBaseInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class GameServerSoldier implements Serializable {
    private List<SoldierBaseInfo> soldierBaseInfoList;

    private LocalDateTime timestamp = LocalDateTime.now();
}


