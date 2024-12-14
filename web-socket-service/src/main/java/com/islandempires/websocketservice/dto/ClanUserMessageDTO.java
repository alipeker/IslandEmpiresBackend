package com.islandempires.websocketservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClanUserMessageDTO {

    private Long id;

    private String serverId;

    private Object data;

}


