package com.islandempires.clanservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClanPrivilegesDTO implements Serializable {
    private Long userId;

    private ClanPrivilegesSubDTO clanPrivileges;
}
