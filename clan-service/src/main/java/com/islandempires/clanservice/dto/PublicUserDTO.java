package com.islandempires.clanservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicUserDTO implements Serializable {

    private Long userId;

    private String username;

    private Long clanId;

    private String serverId;

}

