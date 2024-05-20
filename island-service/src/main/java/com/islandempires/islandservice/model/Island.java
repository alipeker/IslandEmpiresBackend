package com.islandempires.islandservice.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document("island")
@NoArgsConstructor
@AllArgsConstructor
public class Island implements Serializable {
    @Id
    @NotBlank
    private String id;

    private String serverId;

    private String name;

    private Long userId;

    private int x;

    private int y;


}