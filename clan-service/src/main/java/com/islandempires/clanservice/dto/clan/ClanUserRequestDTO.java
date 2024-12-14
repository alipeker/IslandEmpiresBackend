package com.islandempires.clanservice.dto.clan;

import com.islandempires.clanservice.enums.ClanJoinRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClanUserRequestDTO implements Serializable {

    private Long requestId;

    private Long userId;

    private String username;

    private Long clanId;

    private String clanName;

    private String clanShortname;

    private ClanJoinRequestStatus clanJoinRequestStatus;

    private String requestLocalDateTime;
}
