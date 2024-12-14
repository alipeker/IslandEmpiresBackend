package com.islandempires.clanservice.dto.clan;

import com.islandempires.clanservice.model.ClanPrivileges;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClanUserDTO implements Serializable {
    private Long userId;
    private String username;
    private ClanPrivilegesDTO clanPrivileges;

}
