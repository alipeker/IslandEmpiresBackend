package com.islandempires.clanservice.mapper;

import com.islandempires.clanservice.dto.ForumContentDTO;
import com.islandempires.clanservice.dto.ForumDTO;
import com.islandempires.clanservice.dto.ForumMessageDTO;
import com.islandempires.clanservice.dto.ServerUserDTO;
import com.islandempires.clanservice.dto.clan.*;
import com.islandempires.clanservice.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ClanMapper {


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClanDTO toClanDTO (Clan clan) {
        if(clan == null) {
            return null;
        }

        ClanDTO clanDTO = new ClanDTO();

        clanDTO.setId(clan.getId());
        clanDTO.setName(clan.getName());
        clanDTO.setClanDescription(clan.getClanDescription());
        clanDTO.setShortName(clan.getShortName());
        clanDTO.setClanJoinTypeEnum(clan.getClanJoinTypeEnum());
        clanDTO.setMaxMemberNumber(clan.getMaxMemberNumber());
        clanDTO.setServerId(clan.getServer_id());
        clanDTO.setMembers(toClanUserDTO(clan.getUsers()));
        clanDTO.setEnemyClans(toClanRelationDTO(clan.getEnemyClans()));
        clanDTO.setFriendClans(toClanRelationDTO(clan.getFriendClans()));
        clanDTO.setFriendClanRequestsFromClan(toClanToClanRequestToClanDTO(clan.getFriendClanRequestsFromClan()));
        clanDTO.setFriendClanRequestsToClan(toClanToClanRequestFromClanDTO(clan.getFriendClanRequestsToClan()));

        clanDTO.setClanToClanFinishWarRequestFromClan(toClanToClanWarRequestFromClanDTO(clan.getClanToClanFinishWarRequestFromClan()));
        clanDTO.setClanToClanFinishWarRequestToClan(toClanToClanWarRequestToClanDTO(clan.getClanToClanFinishWarRequestToClan()));

        clanDTO.setCreatedDateTime(clan.getCreatedDateTime().toString());
        ServerUser founder = clan.getFounder();
        if(founder != null) {
            clanDTO.setFounder(new ClanUserDTO(founder.getUser_id(), founder.getUser().getUsername(),
                    new ClanPrivilegesDTO(founder.getClanPrivileges().getId(),
                            founder.getClanPrivileges().getAdmin(),
                            founder.getClanPrivileges().getInvite(),
                            founder.getClanPrivileges().getDiplomacy(),
                            founder.getClanPrivileges().getForumModerator(),
                            founder.getClanPrivileges().getSecretForum())));
        }

        clanDTO.setClanJoinRequests(toJoinRequestDTO(clan.getClanJoinRequests()));
        clanDTO.setClanInviteRequests(toInviteRequestDTO(clan.getClanInviteRequests()));
        clanDTO.setForum(toForumDTO(clan.getForum()));
        return clanDTO;
    }

    public ForumDTO toForumDTO(Forum forum) {
        ForumDTO forumDTO = new ForumDTO();
        forumDTO.setForumContentDTOList(
                forum.getForumContentList()
                .stream()
                .map(forumContent ->
                        new ForumContentDTO(
                                forumContent.getId(),
                                forumContent.getForumMessageList()
                                        .stream()
                                        .map(this::toForumMessageDTO)
                                        .toList(),
                                new ServerUserDTO(forumContent.getServerUser().getUser().getUserId(),
                                        forumContent.getServerUser().getUser().getUsername()),
                                forumContent.getHeader(),
                                toForumMessageDTO(forumContent.getForumMessage()),
                                forumContent.getLocalDateTime().toString(),
                                forumContent.getUpdatedLocalDateTime().toString())
                )
                .collect(Collectors.toList()));
        return forumDTO;
    }

    public ForumMessageDTO toForumMessageDTO(ForumMessage forumMessage) {
        return new ForumMessageDTO(forumMessage.getId(), forumMessage.getBody(), forumMessage.getAttachment(), forumMessage.getLocalDateTime().toString());
    }

    public List<ClanUserRequestDTO> toJoinRequestDTO(Set<ClanJoinRequests> clanJoinRequests) {
        return clanJoinRequests
                .stream()
                .map(clanJoinRequests1 -> new ClanUserRequestDTO(
                        clanJoinRequests1.getId(),
                        clanJoinRequests1.getServerUser().getUser().getUserId(),
                        clanJoinRequests1.getServerUser().getUser().getUsername(),
                        clanJoinRequests1.getClan().getId(),
                        clanJoinRequests1.getClan().getName(),
                        clanJoinRequests1.getClan().getShortName(),
                        clanJoinRequests1.getClanJoinRequestStatus(),
                        clanJoinRequests1.getRequestLocalDateTime().toString()))
                .collect(Collectors.toList());
    }

    public List<ClanUserRequestDTO> toInviteRequestDTO(Set<ClanInviteRequest> clanJoinRequests) {
        return clanJoinRequests
                .stream()
                .map(clanJoinRequests1 -> new ClanUserRequestDTO(
                        clanJoinRequests1.getId(),
                        clanJoinRequests1.getServerUser().getUser().getUserId(),
                        clanJoinRequests1.getServerUser().getUser().getUsername(),
                        clanJoinRequests1.getClan().getId(),
                        clanJoinRequests1.getClan().getName(),
                        clanJoinRequests1.getClan().getShortName(),
                        clanJoinRequests1.getClanJoinRequestStatus(),
                        clanJoinRequests1.getRequestLocalDateTime().toString())
                )
                .collect(Collectors.toList());
    }

    public List<ClanToClanRequestDTO> toClanToClanRequestFromClanDTO(Set<ClanToClanFriendRequest> clanToClanFriendRequestsFromClan) {
        return clanToClanFriendRequestsFromClan.stream()
                .map(clanToClanFriendRequest ->
                        new ClanToClanRequestDTO(
                                clanToClanFriendRequest.getId(),
                                clanToClanFriendRequest.getReceiverClan().getId(),
                                clanToClanFriendRequest.getReceiverClan().getName(),
                                clanToClanFriendRequest.getReceiverClan().getShortName(),
                                clanToClanFriendRequest.getClanToClanRequestStatus(),
                                clanToClanFriendRequest.getRequestDateTime().toString()))
                .collect(Collectors.toList());
    }

    public List<ClanToClanRequestDTO> toClanToClanRequestToClanDTO(Set<ClanToClanFriendRequest> clanToClanFriendRequestsFromClan) {
        return clanToClanFriendRequestsFromClan.stream()
                .map(clanToClanFriendRequest ->
                        new ClanToClanRequestDTO(
                                clanToClanFriendRequest.getId(),
                                clanToClanFriendRequest.getSenderClan().getId(),
                                clanToClanFriendRequest.getSenderClan().getName(),
                                clanToClanFriendRequest.getSenderClan().getShortName(),
                                clanToClanFriendRequest.getClanToClanRequestStatus(),
                                clanToClanFriendRequest.getRequestDateTime().toString()))
                .collect(Collectors.toList());
    }

    public List<ClanToClanRequestDTO> toClanToClanWarRequestFromClanDTO(Set<ClanToClanFinishWarRequest> clanToClanFriendRequestsFromClan) {
        return clanToClanFriendRequestsFromClan.stream()
                .map(clanToClanFriendRequest ->
                        new ClanToClanRequestDTO(
                                clanToClanFriendRequest.getId(),
                                clanToClanFriendRequest.getReceiverClan().getId(),
                                clanToClanFriendRequest.getReceiverClan().getName(),
                                clanToClanFriendRequest.getReceiverClan().getShortName(),
                                null,
                                clanToClanFriendRequest.getRequestDateTime().toString()))
                .collect(Collectors.toList());
    }

    public List<ClanToClanRequestDTO> toClanToClanWarRequestToClanDTO(Set<ClanToClanFinishWarRequest> clanToClanFriendRequestsFromClan) {
        return clanToClanFriendRequestsFromClan.stream()
                .map(clanToClanFriendRequest ->
                        new ClanToClanRequestDTO(
                                clanToClanFriendRequest.getId(),
                                clanToClanFriendRequest.getSenderClan().getId(),
                                clanToClanFriendRequest.getSenderClan().getName(),
                                clanToClanFriendRequest.getSenderClan().getShortName(),
                                null,
                                clanToClanFriendRequest.getRequestDateTime().toString()))
                .collect(Collectors.toList());
    }

    public List<ClanUserDTO> toClanUserDTO(Set<ServerUser> userSet) {
        return userSet.stream()
                .map(serverUser -> new ClanUserDTO(
                        serverUser.getUser_id(),
                        serverUser.getUser().getUsername(),
                        serverUser.getClanPrivileges() != null ? new ClanPrivilegesDTO(serverUser.getClanPrivileges().getId(),
                                serverUser.getClanPrivileges().getAdmin(),
                                serverUser.getClanPrivileges().getInvite(),
                                serverUser.getClanPrivileges().getDiplomacy(),
                                serverUser.getClanPrivileges().getForumModerator(),
                                serverUser.getClanPrivileges().getSecretForum()) : null
                ))
                .collect(Collectors.toList());
    }

    public List<ClanRelationDTO> toClanRelationDTO(Set<Clan> clanSet) {
        return clanSet
                .stream()
                .map(clan -> new ClanRelationDTO(clan.getId(), clan.getName(), clan.getShortName()))
                .collect(Collectors.toList());
    }
}
