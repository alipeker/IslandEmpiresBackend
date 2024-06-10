package com.islandempires.gameserverservice.model;

import com.islandempires.gameserverservice.model.soldier.SoldierBaseInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Data
@RequiredArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class GameServerSoldier implements Serializable {
    @Id
    private String id;

    private List<SoldierBaseInfo> soldierBaseInfoList;

    private LocalDateTime timestamp = LocalDateTime.now();
}


