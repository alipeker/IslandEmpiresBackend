package com.islandempires.clanservice.dto;

import com.islandempires.clanservice.enums.ClanJoinTypeEnum;
import com.islandempires.clanservice.model.UrlWrapper;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrUpdateClanDTO {

    private Long id;

    @Size(min = 3, max = 30)
    private String name;

    @Size(min = 1, max = 7)
    private String shortName;

    private String serverId;

    @Size(max = 1000)
    private String clanDescription;

    @Enumerated(EnumType.STRING)
    private ClanJoinTypeEnum clanJoinTypeEnum = ClanJoinTypeEnum.PUBLIC;

    @Valid
    private List<UrlWrapper> attachments = new ArrayList<>();

}
