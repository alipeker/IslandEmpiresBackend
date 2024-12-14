package com.islandempires.clanservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClanPrivilegesSubDTO implements Serializable {
    private Boolean admin;

    private Boolean invite;

    private Boolean diplomacy;

    private Boolean forumModerator;

    private Boolean secretForum;
}
