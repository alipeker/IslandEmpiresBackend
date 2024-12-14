package com.islandempires.clanservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaClanUserDTO implements Serializable {
    private Long userId;
    private String serverId;
}
