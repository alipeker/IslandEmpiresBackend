package com.islandempires.clanservice.dto.clan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClanRelationDTO implements Serializable {
    private Long clanId;

    private String clanName;

    private String clanShortName;

}
