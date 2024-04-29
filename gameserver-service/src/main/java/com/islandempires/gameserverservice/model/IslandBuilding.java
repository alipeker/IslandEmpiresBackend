package com.islandempires.gameserverservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;


@Data
@RequiredArgsConstructor
@EqualsAndHashCode
public class IslandBuilding implements Serializable {
    @Id
    private String id;

    private GameServer gameServer;

}
