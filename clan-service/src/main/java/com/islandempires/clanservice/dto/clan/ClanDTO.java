package com.islandempires.clanservice.dto.clan;

import com.islandempires.clanservice.dto.ForumDTO;
import com.islandempires.clanservice.enums.ClanJoinTypeEnum;
import com.islandempires.clanservice.model.ClanToClanFinishWarRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClanDTO implements Serializable {
    private Long id;

    private String name;

    private String shortName;

    private String clanDescription;

    private ClanJoinTypeEnum clanJoinTypeEnum;

    private ClanUserDTO founder;

    private String serverId;

    private int maxMemberNumber;

    private List<ClanUserDTO> members = new ArrayList<>();

    private List<ClanRelationDTO> enemyClans = new ArrayList<>();

    private List<ClanRelationDTO> friendClans = new ArrayList<>();

    private List<ClanToClanRequestDTO> friendClanRequestsToClan = new ArrayList<>();

    private List<ClanToClanRequestDTO> friendClanRequestsFromClan = new ArrayList<>();

    private List<ClanToClanRequestDTO> clanToClanFinishWarRequestFromClan = new ArrayList<>();

    private List<ClanToClanRequestDTO> clanToClanFinishWarRequestToClan = new ArrayList<>();

    private List<ClanUserRequestDTO> clanJoinRequests = new ArrayList<>();

    private List<ClanUserRequestDTO> clanInviteRequests = new ArrayList<>();

    private String createdDateTime;

    private ForumDTO forum;
}

