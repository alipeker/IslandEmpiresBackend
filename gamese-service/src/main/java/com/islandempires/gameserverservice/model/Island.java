package com.islandempires.gameserverservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Document
@Data
@RequiredArgsConstructor
@Table(name = "Island")
@EqualsAndHashCode
public class Island {

    @Id
    private String islandId;

    private long timestamp = new Date().getTime();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gameServer_id")
    private GamerServer gamerServer;
}
