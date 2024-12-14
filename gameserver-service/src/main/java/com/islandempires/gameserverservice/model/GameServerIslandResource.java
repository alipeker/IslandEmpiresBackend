package com.islandempires.gameserverservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class GameServerIslandResource implements Serializable {

    private IslandResource islandResource;

    private LocalDateTime timestamp = LocalDateTime.now();

}

