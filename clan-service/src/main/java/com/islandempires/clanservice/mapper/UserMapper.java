package com.islandempires.clanservice.mapper;

import com.islandempires.clanservice.dto.clan.ClanPrivilegesDTO;
import com.islandempires.clanservice.dto.clan.ClanUserDTO;
import com.islandempires.clanservice.dto.clan.ClanUserRequestDTO;
import com.islandempires.clanservice.dto.user.UserPrivateDTO;
import com.islandempires.clanservice.dto.user.UserRelationDTO;
import com.islandempires.clanservice.enums.FriendRequestStatus;
import com.islandempires.clanservice.model.ClanPrivileges;
import com.islandempires.clanservice.model.ServerUser;
import com.islandempires.clanservice.model.ServerUserFriend;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class UserMapper {

    private final ClanMapper clanMapper;

    private final ConversationMapper conversationMapper;

    public UserPrivateDTO toUserDTO(ServerUser serverUser) {
        UserPrivateDTO userDTO = new UserPrivateDTO();
        userDTO.setId(serverUser.getUser().getUserId());

        if(serverUser.getClan() != null) {
            userDTO.setClan(clanMapper.toClanDTO(serverUser.getClan()));
        }

        userDTO.setCanUserCreateClan(serverUser.getCanUserCreateClan());
        userDTO.setCanUserJoinClan(serverUser.getCanUserJoinClan());

        ClanPrivileges clanPrivileges = serverUser.getClanPrivileges();
        if(clanPrivileges != null) {
            ClanPrivilegesDTO clanPrivilegesDTO = new ClanPrivilegesDTO(clanPrivileges.getId(),
                    clanPrivileges.getAdmin(),
                    clanPrivileges.getInvite(),
                    clanPrivileges.getDiplomacy(),
                    clanPrivileges.getForumModerator(),
                    clanPrivileges.getSecretForum());

            userDTO.setClanPrivileges(clanPrivilegesDTO);
        }


        if(serverUser.getConversations() != null && serverUser.getConversations().size() > 0) {
            userDTO.setConversations(
                    serverUser.getConversations()
                            .stream()
                            .map(conversationMapper::toConversationDTO)
                            .collect(Collectors.toList())
            );
        }

        userDTO.setUserJoinLocalDateTime(serverUser.getUserJoinLocalDateTime());

        if(serverUser.getBlockList() != null && serverUser.getBlockList().size() > 0) {
            userDTO.setBlockList(
                    serverUser.getBlockList()
                            .stream()
                            .map(serverUserFriend ->
                                    new ClanUserDTO(
                                            serverUserFriend.getUser().getUserId(),
                                            serverUserFriend.getUser().getUsername(),
                                            null))
                            .collect(Collectors.toList())
            );
        }

        List<ServerUserFriend> combinedFriendList = Stream.concat(serverUser.getFriendList().stream(), serverUser.getReceiverList().stream())
                .toList();

        if(combinedFriendList.size() > 0) {
            userDTO.setFriendList(
                    combinedFriendList
                            .stream()
                            .map(serverUserFriend ->
                                    new UserRelationDTO(
                                            serverUserFriend.getId(),
                                            serverUserFriend.getSenderUser().getUser().getUserId(),
                                            serverUserFriend.getSenderUser().getUser().getUsername(),
                                            serverUserFriend.getReceiverUser().getUser().getUserId(),
                                            serverUserFriend.getReceiverUser().getUser().getUsername(),
                                            serverUserFriend.getFriendRequestStatus(),
                                            serverUserFriend.getTimestamp()
                                            ))
                            .collect(Collectors.toList())
            );
        }


        if(serverUser.getClanJoinRequests() != null && serverUser.getClanJoinRequests().size() > 0) {
            userDTO.setClanJoinRequests(
                    serverUser.getClanJoinRequests()
                            .stream()
                            .map(clanJoinRequests ->
                                    new ClanUserRequestDTO(clanJoinRequests.getId(),
                                    clanJoinRequests.getServerUser().getUser_id(),
                                    clanJoinRequests.getServerUser().getUser().getUsername(),
                                    clanJoinRequests.getClan().getId(),
                                    clanJoinRequests.getClan().getName(),
                                    clanJoinRequests.getClan().getShortName(),
                                    clanJoinRequests.getClanJoinRequestStatus(),
                                    clanJoinRequests.getRequestLocalDateTime().toString()))
                            .collect(Collectors.toList())
            );
        }

        if(serverUser.getClanInviteRequestSet() != null && serverUser.getClanInviteRequestSet().size() > 0) {
            userDTO.setClanInviteRequestSet(
                    serverUser.getClanInviteRequestSet()
                            .stream()
                            .map(clanJoinRequests ->
                                    new ClanUserRequestDTO(clanJoinRequests.getId(),
                                            clanJoinRequests.getServerUser().getUser_id(),
                                            clanJoinRequests.getServerUser().getUser().getUsername(),
                                            clanJoinRequests.getClan().getId(),
                                            clanJoinRequests.getClan().getName(),
                                            clanJoinRequests.getClan().getShortName(),
                                            clanJoinRequests.getClanJoinRequestStatus(),
                                            clanJoinRequests.getRequestLocalDateTime().toString()
                                            ))
                            .collect(Collectors.toList())
            );
        }

        return userDTO;
    }
}
