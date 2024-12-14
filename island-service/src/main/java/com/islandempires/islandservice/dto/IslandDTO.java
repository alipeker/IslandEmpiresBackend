package com.islandempires.islandservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IslandDTO {
    private String id;

    private String serverId;

    private String name;

    private Long userId;

    private int x;

    private int y;

    private LocalDateTime localDateTime;

    private Long population;
}
