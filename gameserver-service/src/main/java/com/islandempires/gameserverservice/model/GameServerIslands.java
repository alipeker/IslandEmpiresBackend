package com.islandempires.gameserverservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@EqualsAndHashCode
@RequiredArgsConstructor
@NoArgsConstructor
public class GameServerIslands implements Serializable {

    @Id
    private String id;

    @NonNull
    private String serverId;

    @NonNull
    private String islandId;

    @NonNull
    private Long userId;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime createdDate;
}
