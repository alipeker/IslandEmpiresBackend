package com.islandempires.gameserverservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

@Document
@Data
@RequiredArgsConstructor
@Table(name = "GamerServer")
@EqualsAndHashCode
public class GamerServer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GamerServer_generator")
    @SequenceGenerator(name="GamerServer_generator", sequenceName = "server.GamerServer_sequence", allocationSize=1)
    private Long id;

    private String serverName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gamerServer", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Island> islandIds;



    private long timestamp = new Date().getTime();
}
