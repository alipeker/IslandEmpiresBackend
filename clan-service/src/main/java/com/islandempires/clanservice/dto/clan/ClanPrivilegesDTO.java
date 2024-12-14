package com.islandempires.clanservice.dto.clan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClanPrivilegesDTO implements Serializable {
    private Long id;

    private Boolean admin = false;

    private Boolean invite = false;

    private Boolean diplomacy = false;

    private Boolean forumModerator = false;

    private Boolean secretForum = false;
}
