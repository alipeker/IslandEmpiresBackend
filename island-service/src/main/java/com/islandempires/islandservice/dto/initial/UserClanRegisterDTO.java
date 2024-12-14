package com.islandempires.islandservice.dto.initial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserClanRegisterDTO implements Serializable {
    private Long id;

    private String username;

    private String serverId;

    public UserClanRegisterDTO(Long id, String serverId) {
        this.id = id;
        this.serverId = serverId;
    }
}
