package com.islandempires.clanservice.dto;

import com.islandempires.clanservice.enums.ClanJoinTypeEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateClanJoinTypeDTO {
    private Long id;

    private String serverId;

    @Enumerated(EnumType.STRING)
    private ClanJoinTypeEnum clanJoinTypeEnum;
}
