package com.islandempires.clanservice.outbox;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerUserEvent implements Serializable {
    private String serverId;
    private Long userId;
}
