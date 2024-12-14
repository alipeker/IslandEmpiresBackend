package com.islandempires.clanservice.dto;

import com.islandempires.clanservice.enums.ClanJoinTypeEnum;
import com.islandempires.clanservice.model.UrlWrapper;
import com.islandempires.clanservice.model.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicClanDTO implements Serializable {

    private Long clanId;

    private String clanName;

    private String clanShortName;

    private String clanDescription;

    @Enumerated(EnumType.STRING)
    private ClanJoinTypeEnum clanJoinTypeEnum;

    private List<User> userList;

    private LocalDateTime localDateTime;

    private List<UrlWrapper> attachments;

    private User founder;

    private String serverId;

}
