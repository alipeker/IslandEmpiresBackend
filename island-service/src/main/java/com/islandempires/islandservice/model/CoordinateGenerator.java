package com.islandempires.islandservice.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("CoordinateGenerator")
@NoArgsConstructor
@AllArgsConstructor
public class CoordinateGenerator {
    @Id
    @NotBlank
    private String id;

    private String serverId;

    private CoordinateValues northWest;

    private CoordinateValues northEast;

    private CoordinateValues southWest;

    private CoordinateValues southEast;
}
