package com.islandempires.clanservice.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClanUserDataMessage {

    private Long id;

    private String serverId;

    private Object data;

}

