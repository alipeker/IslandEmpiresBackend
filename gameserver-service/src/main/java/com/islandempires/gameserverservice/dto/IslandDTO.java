package com.islandempires.gameserverservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IslandDTO {
    private String id;

    private String name;

    private Long userId;

    private int x;

    private int y;
}
