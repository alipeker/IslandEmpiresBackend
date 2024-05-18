package com.islandempires.sessiontrackingservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Document("IslandRecord")
@NoArgsConstructor
@AllArgsConstructor
public class IslandRecord implements Serializable {
    @Id
    private String islandId;

    private LocalDateTime localDateTime;

}
