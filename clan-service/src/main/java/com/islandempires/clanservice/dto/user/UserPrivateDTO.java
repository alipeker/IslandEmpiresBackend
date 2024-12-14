package com.islandempires.clanservice.dto.user;

import com.islandempires.clanservice.dto.clan.ClanDTO;
import com.islandempires.clanservice.dto.clan.ClanPrivilegesDTO;
import com.islandempires.clanservice.dto.clan.ClanUserDTO;
import com.islandempires.clanservice.dto.clan.ClanUserRequestDTO;
import com.islandempires.clanservice.dto.conversation.ConversationDTO;
import com.islandempires.clanservice.model.ClanInviteRequest;
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
public class UserPrivateDTO implements Serializable {

    private Long id;

    private ClanDTO clan;

    private Boolean canUserJoinClan;

    private Boolean canUserCreateClan;

    private int maxClanMemberNumber;

    private List<UserRelationDTO> friendList = new ArrayList<>();

    private List<ClanUserDTO> blockList = new ArrayList<>();

    private List<ConversationDTO> conversations = new ArrayList<>();

    private LocalDateTime clanJoinDateTime;

    private LocalDateTime userJoinLocalDateTime;

    private ClanPrivilegesDTO clanPrivileges;

    private List<ClanUserRequestDTO> clanJoinRequests = new ArrayList<>();

    private List<ClanUserRequestDTO> clanInviteRequestSet = new ArrayList<>();


    public void setFriendList() {

    }

}
