package com.islandempires.mapservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "islands")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IslandCombined {

    @Id
    private String id;

    private String serverId;
    private String name;
    private Long userId;
    private int x;
    private int y;
    private Integer population;
    private String username;
}