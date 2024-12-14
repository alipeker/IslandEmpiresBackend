package com.islandempires.gameserverservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class GameServerIslands implements Serializable {
    private String serverId;


    private String islandId;

    private Long userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime createdDate;
}
