package com.islandempires.websocketservice.model;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Document("Session")
@NoArgsConstructor
@AllArgsConstructor
public class Session implements Serializable {

    @Id
    @NotBlank
    private String sessionId;

    private Long userId;

    private String jwtToken;

    private String serverId;

    private LocalDateTime lastActive;

}
