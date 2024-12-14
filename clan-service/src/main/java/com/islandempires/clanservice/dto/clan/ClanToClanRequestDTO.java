package com.islandempires.clanservice.dto.clan;

import com.islandempires.clanservice.enums.ClanToClanRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClanToClanRequestDTO implements Serializable {
    private Long requestId;

    private Long clanId;

    private String clanName;

    private String clanShortName;

    private ClanToClanRequestStatus clanToClanRequestStatus;

    private String localDateTime;
}
