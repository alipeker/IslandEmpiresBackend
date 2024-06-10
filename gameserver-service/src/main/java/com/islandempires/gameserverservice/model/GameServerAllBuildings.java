package com.islandempires.gameserverservice.model;

import com.islandempires.gameserverservice.model.building.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Document
@Data
@RequiredArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class GameServerAllBuildings implements Serializable {
    @Id
    private String id;

    private AllBuildings allBuildings;

    private LocalDateTime timestamp = LocalDateTime.now();

}

