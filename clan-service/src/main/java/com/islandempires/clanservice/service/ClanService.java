package com.islandempires.clanservice.service;

import com.islandempires.clanservice.dto.*;
import com.islandempires.clanservice.dto.clan.ClanDTO;
import com.islandempires.clanservice.dto.user.UserPrivateDTO;
import com.islandempires.clanservice.enums.ClanJoinRequestStatus;
import com.islandempires.clanservice.enums.ClanJoinTypeEnum;
import com.islandempires.clanservice.enums.ClanToClanRequestStatus;
import com.islandempires.clanservice.exception.CustomRunTimeException;
import com.islandempires.clanservice.exception.ExceptionE;
import com.islandempires.clanservice.mapper.ClanMapper;
import com.islandempires.clanservice.model.*;
import com.islandempires.clanservice.repository.*;
import com.islandempires.clanservice.dto.KafkaClanUserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ClanService {

     private static final Logger logger = LoggerFactory.getLogger(ClanService.class);

    @NonNull
    private final ClanRepository clanRepository;

    @NonNull
    private final ServerUserRepository serverUserRepository;

    @NonNull
    private final ClanInviteRequestsRepository clanInviteRequestsRepository;

    @NonNull
    private final ClanJoinRequestsRepository clanJoinRequestsRepository;

    @NonNull
    private final ClanToClanFinishWarRequestRepository clanToClanFinishWarRequestRepository;

    @NonNull
    private final ClanToClanFriendRequestRepository clanToClanFriendRequestRepository;

    @NonNull
    private final ReserveIslandRepository reserveIslandRepository;

    @NonNull
    private final ForumRepository forumRepository;

    @NonNull
    private final ClanMapper clanMapper;

    @NonNull
    private final UserService userService;

    @NonNull
    private final ServerRepository serverRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClanDTO getClan(Long clanId, Long userId) {
        Clan clan = clanRepository.findById(clanId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(clan.getUserWithId(userId).isEmpty()) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        return clanMapper.toClanDTO(clan);
    }

    public ClanDTO getClanWithId(Long clanId) {
        return clanMapper.toClanDTO(clanRepository.findById(clanId).orElseThrow());
    }

    public ClanDTO getClanWithUser(String serverId, Long userId) {
        entityManager.clear();
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow();
        return clanMapper.toClanDTO(serverUser.getClan());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Clan createClan(CreateOrUpdateClanDTO createOrUpdateClanDTO, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, createOrUpdateClanDTO.getServerId())
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(!serverUser.getCanUserCreateClan()) {
            throw new CustomRunTimeException(ExceptionE.EMBASSY_LVL_CREATE_CLAN_ERROR);
        }

        if(serverUser.getClan() != null ||
           clanRepository.findByServerIdAndNameOrShortNameAndIdNot(
                   createOrUpdateClanDTO.getServerId(), createOrUpdateClanDTO.getId(), createOrUpdateClanDTO.getName(),
                   createOrUpdateClanDTO.getShortName()).isPresent())
        {
            throw new CustomRunTimeException(ExceptionE.ALREADY_EXIST);
        }

        Clan newClan = new Clan();
        newClan.setName(createOrUpdateClanDTO.getName());
        newClan.setShortName(createOrUpdateClanDTO.getShortName());
        newClan.setServer_id(createOrUpdateClanDTO.getServerId());
        newClan.setClanDescription("This clan was established by " + serverUser.getUser().getUsername() + "! Please contact them.");
        newClan.setAttachments(createOrUpdateClanDTO.getAttachments());
        newClan.setCreatedDateTime(LocalDateTime.now());
        newClan.setClanJoinTypeEnum(createOrUpdateClanDTO.getClanJoinTypeEnum());
        newClan.setForum(new Forum(null, null, newClan));

        serverUser.initializeWithAdminGrantPrivileges();
        newClan.addClanMember(serverUser);
        newClan.setFounder_server_user_id(serverUser.getId());
        serverUser.grantAdminPrivileges();
        Clan clan = clanRepository.save(newClan);
        return clan;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Clan updateClan(CreateOrUpdateClanDTO createOrUpdateClanDTO, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, createOrUpdateClanDTO.getServerId())
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(serverUser.getClan() != null ||
                clanRepository.findByServerIdAndNameOrShortNameAndIdNot(
                        createOrUpdateClanDTO.getServerId(), createOrUpdateClanDTO.getId(), createOrUpdateClanDTO.getName(), createOrUpdateClanDTO.getShortName()
                ).isPresent())
        {
            throw new CustomRunTimeException(ExceptionE.ALREADY_EXIST);
        }

        Clan newClan = new Clan();
        newClan.setName(createOrUpdateClanDTO.getName());
        newClan.setShortName(createOrUpdateClanDTO.getShortName());
        newClan.setClanDescription(createOrUpdateClanDTO.getClanDescription());
        newClan.setAttachments(createOrUpdateClanDTO.getAttachments());
        newClan.setClanJoinTypeEnum(createOrUpdateClanDTO.getClanJoinTypeEnum());

        Clan savedClan = clanRepository.save(newClan);
        return savedClan;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClanDTO updateNameShortname(UpdateNameShortnameDTO updateNameShortnameDTO, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, updateNameShortnameDTO.getServerId())
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(serverUser.getClan() == null) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }

        if(clanRepository.findByServerIdAndNameOrShortNameAndIdNot(
                updateNameShortnameDTO.getServerId(), updateNameShortnameDTO.getId(), updateNameShortnameDTO.getName(), updateNameShortnameDTO.getShortName()).isPresent())
        {
            throw new CustomRunTimeException(ExceptionE.ALREADY_EXIST);
        }

        if(!serverUser.getClanPrivileges().getAdmin()) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        Clan clan = serverUser.getClan();
        clan.setName(updateNameShortnameDTO.getName());
        clan.setShortName(updateNameShortnameDTO.getShortName());

        Clan savedClan = clanRepository.save(clan);
        return clanMapper.toClanDTO(savedClan);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClanDTO updateJoinType(UpdateClanJoinTypeDTO updateClanJoinTypeDTO, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, updateClanJoinTypeDTO.getServerId())
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(serverUser.getClan() == null) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }

        if(!serverUser.getClanPrivileges().getAdmin()) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        Clan newClan = serverUser.getClan();
        newClan.setClanJoinTypeEnum(updateClanJoinTypeDTO.getClanJoinTypeEnum());

        Clan savedClan = clanRepository.save(newClan);
        return clanMapper.toClanDTO(savedClan);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClanDTO updateDescription(UpdateDescriptionDTO updateDescriptionDTO, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, updateDescriptionDTO.getServerId())
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(serverUser.getClan() == null) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }

        if(!serverUser.getClanPrivileges().getAdmin()) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        Clan newClan = serverUser.getClan();
        newClan.setClanDescription(updateDescriptionDTO.getClanDescription());

        Clan savedClan = clanRepository.save(newClan);
        return clanMapper.toClanDTO(savedClan);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String deleteCLan(Long clanId, Long userId) {
        Clan clan = clanRepository.findById(clanId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        String serverId = clan.getServer_id();

        Optional<ServerUser> optionalServerUser = clan.getUserWithId(userId);

        if(optionalServerUser.isEmpty()
                || optionalServerUser.get().getClanPrivileges() == null
                || !optionalServerUser.get().getClanPrivileges().isAdminPrivileges()) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        clanToClanFriendRequestRepository.deleteByClanId(clan.getId());
        clanToClanFinishWarRequestRepository.deleteByClanId(clan.getId());
        serverUserRepository.updateClanToNullByClanId(clan.getId());
        clanInviteRequestsRepository.deleteByClan(clan);
        reserveIslandRepository.deleteByClan(clan);

        clan.getFriendClans().forEach(friendClan -> friendClan.deleteFriendClan(clan));
        clan.getEnemyClans().forEach(enemyClan -> enemyClan.deleteEnemyClan(clan));
        clan.getForum().setForumContentList(new HashSet<>());
        clan.setForum(null);
        clanRepository.save(clan);
        clanRepository.deleteClanById(clanId);
        return serverId;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long leaveClan(String serverId, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        Clan clan = serverUser.getClan();
        if(clan == null) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }

        if(serverUser.getClanPrivileges() != null &&
                serverUser.getClanPrivileges().isAdminPrivileges() &&
                clan.getUsers().size() > 1) {
            List<ServerUser> adminList = clan.getUsers().stream().filter(serverUserMember -> serverUserMember.getClanPrivileges().getAdmin()).toList();
            if(adminList.size() == 1) {
                throw new CustomRunTimeException(ExceptionE.ADMIN_ERROR);
            }
        }

        serverUser.setClan(null);
        serverUser.removeClanPrivileges();
        serverUser.setClanJoinDateTime(null);
        serverUserRepository.save(serverUser);
        return clan.getId();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long inviteUser(String serverId, Long inviteUserId, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(serverUser.getClanPrivileges() == null || serverUser.getClan() == null  || !serverUser.getClanPrivileges().isInvitePrivileges()) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        ServerUser inviteServerUser = serverUserRepository.findByUserIdAndServerId(inviteUserId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(clanInviteRequestsRepository.findByClanAndServerUser(serverUser.getClan(), inviteServerUser).isPresent()) {
            throw new CustomRunTimeException(ExceptionE.ALREADY_EXIST);
        }

        ClanInviteRequest clanJoinRequests = new ClanInviteRequest(null, serverUser.getClan(), inviteServerUser,
                ClanJoinRequestStatus.PENDING, LocalDateTime.now());

        clanInviteRequestsRepository.save(clanJoinRequests);

        return serverUser.getClan().getId();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserPrivateDTO joinRequest(Long clanId, Long userId) {
        Clan clan = clanRepository.findById(clanId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, clan.getServer_id())
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        validateClanJoinType(clan);
        validateExistingClanMembership(serverUser);

        if(clan.isMaxUserExceeded()) {
            throw new CustomRunTimeException(ExceptionE.CLAN_MEMBER_MAX_NUMBER_ERROR);
        }

        if (clan.getClanJoinTypeEnum().equals(ClanJoinTypeEnum.PUBLIC)) {
            handlePublicJoinRequest(clan, serverUser);
        } else {
            handleNonPublicJoinRequest(clan, serverUser);
        }
        return userService.getServerUserAll(userId, clan.getServer_id());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void validateClanJoinType(Clan clan) {
        if (clan.getClanJoinTypeEnum().equals(ClanJoinTypeEnum.INVITE_ONLY)) {
            throw new CustomRunTimeException(ExceptionE.INVITE_ONLY);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void validateExistingClanMembership(ServerUser serverUser) {
        if (serverUser.getClan() != null) {
            if (serverUser.getClanPrivileges() != null && serverUser.getClanPrivileges().isAdminPrivileges()
                    && serverUser.getClan().getUsers().size() > 1) {
                List<ServerUser> adminList = serverUser.getClan()
                        .getUsers().stream()
                        .filter(user -> user.getClanPrivileges().getAdmin())
                        .toList();
                if (adminList.size() == 1) {
                    throw new CustomRunTimeException(ExceptionE.ADMIN_ERROR);
                }
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void handlePublicJoinRequest(Clan clan, ServerUser serverUser) {
        serverUser.setClan(null);
        clan.addClanMember(serverUser);
        clanRepository.save(clan);
        deleteJoinOrInviteRequest(clan, serverUser);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void handleNonPublicJoinRequest(Clan clan, ServerUser serverUser) {
        if (isExistingJoinRequestOrMember(clan, serverUser)) {
            throw new CustomRunTimeException(ExceptionE.ALREADY_EXIST);
        }

        ClanJoinRequests clanJoinRequests = new ClanJoinRequests(null, clan, serverUser, ClanJoinRequestStatus.PENDING, LocalDateTime.now());

        if(clanJoinRequestsRepository.existsByClanIdAndServerUserId(clan.getId(), serverUser.getId())) {
            throw new CustomRunTimeException(ExceptionE.ALREADY_EXIST);
        }

        clanJoinRequestsRepository.save(clanJoinRequests);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private boolean isExistingJoinRequestOrMember(Clan clan, ServerUser serverUser) {
        return clanJoinRequestsRepository.findByClanAndServerUser(serverUser.getClan(), serverUser).isPresent()
                || clan.getUserWithId(serverUser.getId()).isPresent();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteJoinOrInviteRequest(Clan clan, ServerUser serverUser) {
        clanJoinRequestsRepository.deleteByClanAndServerUser(clan, serverUser);
        clanInviteRequestsRepository.deleteByClanAndServerUser(clan, serverUser);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String approveServerUserInviteRequest(Long requestId, Long userId) {
        ClanInviteRequest clanInviteRequest = clanInviteRequestsRepository.findById(requestId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(!clanInviteRequest.getServerUser().getUser_id().equals(userId)) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        if(clanInviteRequest.getClan().isMaxUserExceeded()) {
            throw new CustomRunTimeException(ExceptionE.CLAN_MEMBER_MAX_NUMBER_ERROR);
        }
        ServerUser serverUser = clanInviteRequest.getServerUser();

        validateExistingClanMembership(serverUser);
        serverUser.setClan(null);
        Clan joiningClan = clanInviteRequest.getClan();
        joiningClan.addClanMember(serverUser);
        clanRepository.save(joiningClan);

        deleteJoinOrInviteRequest(joiningClan, serverUser);
        return joiningClan.getServer_id();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String disapproveServerUserInviteRequest(Long requestId, Long userId) {
        ClanInviteRequest clanInviteRequest = clanInviteRequestsRepository.findById(requestId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        Long clanId = clanInviteRequest.getClan().getId();

        if(!clanInviteRequest.getServerUser().getUser_id().equals(userId)) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        clanInviteRequestsRepository.deleteById(clanInviteRequest.getId());

        return clanInviteRequest.getServerUser().getServer_id();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long removeUserInviteRequest(Long requestId, Long userId) {
        ClanInviteRequest clanInviteRequest = clanInviteRequestsRepository.findById(requestId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        Long clanId = clanInviteRequest.getClan().getId();

        Optional<ServerUser> optionalServerUser = clanInviteRequest.getClan().getUserWithId(userId);

        if(optionalServerUser.isEmpty()
                || optionalServerUser.get().getClanPrivileges() == null
                || !optionalServerUser.get().getClanPrivileges().isInvitePrivileges()) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        clanInviteRequestsRepository.deleteById(requestId);
        return clanId;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long approveUserJoinClanRequest(Long requestId, Long userId) {
        ClanJoinRequests clanJoinRequests = clanJoinRequestsRepository.findById(requestId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(clanJoinRequests.getClan() == null || clanJoinRequests.getServerUser() == null) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }

        if(clanJoinRequests.getClan().isMaxUserExceeded()) {
            throw new CustomRunTimeException(ExceptionE.CLAN_MEMBER_MAX_NUMBER_ERROR);
        }

        ServerUser requestServerUser = clanJoinRequests.getServerUser();
        Clan clan = clanJoinRequests.getClan();

        validateExistingClanMembership(requestServerUser);

        ServerUser serverUser = clanJoinRequests.getClan().getUserWithId(userId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(serverUser.getClanPrivileges() == null || !serverUser.getClanPrivileges().isInvitePrivileges()) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        clan.addClanMember(requestServerUser);
        clanRepository.save(clan);

        deleteJoinOrInviteRequest(clan, requestServerUser);
        return clan.getId();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long disapproveUserJoinClanRequest(Long requestId, Long userId) {
        ClanJoinRequests clanJoinRequests = clanJoinRequestsRepository.findById(requestId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(clanJoinRequests.getClan() == null || clanJoinRequests.getServerUser() == null) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }

        ServerUser serverUser = clanJoinRequests.getClan().getUserWithId(userId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(serverUser.getClanPrivileges() == null || !serverUser.getClanPrivileges().isInvitePrivileges()) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        clanJoinRequests.setClanJoinRequestStatus(ClanJoinRequestStatus.REJECTED);
        clanJoinRequestsRepository.save(clanJoinRequests);
        return clanJoinRequests.getClan().getId();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void kickUserFromClan(Long clanId, Long kickUserId, Long userId) {
        Clan clan = clanRepository.findById(clanId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(!clan.isUserAdmin(userId) || clan.isUserAdmin(kickUserId)) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        Optional<ServerUser> kickServerUser = clan.getUserWithId(kickUserId);
        if(kickServerUser.isEmpty()) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }

        if(kickServerUser.get().isClanAdmin()) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        deleteJoinOrInviteRequest(clan, kickServerUser.get());
        kickServerUser.get().setClan(null);
        kickServerUser.get().removeClanPrivileges();
        serverUserRepository.save(kickServerUser.get());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void finishWarClanRequest(Long clanId, Long enemyClanId, Long userId) {
        if(Objects.equals(clanId, enemyClanId)) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }

        Clan senderClan = clanRepository.findById(clanId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(senderClan.getEnemyClans().stream().noneMatch(clan -> clan.getId().equals(enemyClanId))) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }

        Clan receiverClan = clanRepository.findById(enemyClanId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        ServerUser serverUser = senderClan.getUserWithId(userId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR));
        if(serverUser.getClanPrivileges() == null || !serverUser.getClanPrivileges().isDiplomacyPrivileges()) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        if(clanToClanFinishWarRequestRepository.findBySenderClanAndReceiverClan(senderClan, receiverClan).isPresent()) {
            throw new CustomRunTimeException(ExceptionE.ALREADY_EXIST);
        }

        ClanToClanFinishWarRequest clanToClanFinishWarRequest = new ClanToClanFinishWarRequest(null, senderClan, receiverClan, LocalDateTime.now());
        clanToClanFinishWarRequestRepository.save(clanToClanFinishWarRequest);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Long> removeMyFinishWarRequest(Long requestId, Long userId) {
        ClanToClanFinishWarRequest finishWarRequest =
                clanToClanFinishWarRequestRepository.findById(requestId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        Clan senderClan = finishWarRequest.getSenderClan();

        ServerUser serverUser = senderClan.getUserWithId(userId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR));

        if(serverUser.getClanPrivileges() == null || !serverUser.getClanPrivileges().isDiplomacyPrivileges()) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        clanToClanFinishWarRequestRepository.deleteById(finishWarRequest.getId());
        return List.of(senderClan.getId(), finishWarRequest.getReceiverClan().getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Long> approveRemoveEnemyClanRequest(Long requestId, Long userId) {
        ClanToClanFinishWarRequest clanToClanFinishWarRequest =
                clanToClanFinishWarRequestRepository.findById(requestId)
                        .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        Clan senderClan = clanToClanFinishWarRequest.getSenderClan();
        Clan receiverClan = clanToClanFinishWarRequest.getReceiverClan();
        ServerUser serverUser = receiverClan.getUserWithId(userId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR));
        if(serverUser.getClanPrivileges() == null || !serverUser.getClanPrivileges().isDiplomacyPrivileges()) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        receiverClan.deleteEnemyClan(senderClan);
        clanRepository.save(receiverClan);
        senderClan.deleteEnemyClan(receiverClan);
        clanRepository.save(senderClan);
        clanToClanFinishWarRequestRepository.deleteBySenderAndReceiverClans(clanToClanFinishWarRequest.getSenderClan(), clanToClanFinishWarRequest.getReceiverClan());

        return List.of(receiverClan.getId(), senderClan.getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Long> disapproveRemoveEnemyClanRequest(Long requestId, Long userId) {
        ClanToClanFinishWarRequest clanToClanFinishWarRequest =
                clanToClanFinishWarRequestRepository.findById(requestId)
                        .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        Clan receiverClan = clanToClanFinishWarRequest.getReceiverClan();
        Clan senderClan = clanToClanFinishWarRequest.getSenderClan();
        ServerUser serverUser = receiverClan.getUserWithId(userId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR));
        if(serverUser.getClanPrivileges() == null || !serverUser.getClanPrivileges().isDiplomacyPrivileges()) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        Long clanId = serverUser.getClan().getId();

        clanToClanFinishWarRequestRepository.deleteById(clanToClanFinishWarRequest.getId());

        return List.of(clanId, senderClan.getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addEnemyClan(Long clanId, Long enemyClanId, Long userId) {
        if(clanId.equals(enemyClanId)) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }
        Clan clan = clanRepository.findById(clanId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        ServerUser serverUser = clan.getUserWithId(userId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        if(serverUser.getClanPrivileges() == null
                || !serverUser.getClanPrivileges().isDiplomacyPrivileges())
        {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        Clan enemyClan = clanRepository.findById(enemyClanId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(clan.getFriendClans().stream().anyMatch(enemyClan1 -> enemyClan1.getId().equals(enemyClan.getId()))) {
            throw new CustomRunTimeException(ExceptionE.ENEMY_CLAN_FRIEND_ERROR);
        }

        clanToClanFriendRequestRepository.deleteBySenderClanIdAndReceiverClanId(clan.getId(), enemyClan.getId());
        clanToClanFriendRequestRepository.deleteBySenderClanIdAndReceiverClanId(enemyClan.getId(), clan.getId());
        clan.addEnemyClan(enemyClan);
        clanRepository.saveAndFlush(clan);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addFriendClanRequest(Long clanId, Long friendClanId, Long userId) {
        if(clanId.equals(friendClanId)) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }

        Clan clan = clanRepository.findById(clanId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        logger.info(userId.toString());
        ServerUser serverUser = clan.getUserWithId(userId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(serverUser.getClanPrivileges() == null
                || !serverUser.getClanPrivileges().isDiplomacyPrivileges())
        {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }
        Clan friendClan = clanRepository.findById(friendClanId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        if(clan.getFriendClans().stream().anyMatch(friendClan1 -> friendClan1.getId().equals(friendClan.getId()))) {
            throw new CustomRunTimeException(ExceptionE.ALREADY_EXIST);
        }
        if(clan.getEnemyClans().stream().anyMatch(enemyClan1 -> enemyClan1.getId().equals(friendClan.getId()))) {
            throw new CustomRunTimeException(ExceptionE.ENEMY_CLAN_ADD_FRIEND_ERROR);
        }
        clan.addFriendClanRequest(friendClan);
        clanRepository.saveAndFlush(clan);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Long> removeFriendClanRequest(Long requestId, String serverId, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, serverId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        if(serverUser.getClanPrivileges() == null
                || !serverUser.getClanPrivileges().isDiplomacyPrivileges())
        {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }


        Clan clan = serverUser.getClan();

        ClanToClanFriendRequest clanToClanFriendRequest = clan.getFriendClanRequest(requestId);
        Long senderId = clanToClanFriendRequest.getSenderClan().getId();
        Long receiverId = clanToClanFriendRequest.getReceiverClan().getId();

        clan.deleteFriendClanRequest(requestId);
        clanRepository.saveAndFlush(clan);

        return List.of(senderId, receiverId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Long> approveFriendClanRequest(Long requestId, Long userId) {
        ClanToClanFriendRequest clanToClanFriendRequest = clanToClanFriendRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(clanToClanFriendRequest.getClanToClanRequestStatus().equals(ClanToClanRequestStatus.REJECTED)) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }

        Clan senderClan = clanToClanFriendRequest.getSenderClan();
        Clan receiverClan = clanToClanFriendRequest.getReceiverClan();

        ServerUser serverUser = receiverClan.getUserWithId(userId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        if(!receiverClan.isUserAdmin(userId)
                || serverUser.getClanPrivileges() == null
                || !serverUser.getClanPrivileges().isDiplomacyPrivileges())
        {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        if(receiverClan.getEnemyClans().stream().anyMatch(enemyclan -> enemyclan.getId().equals(senderClan.getId()))) {
            throw new CustomRunTimeException(ExceptionE.ENEMY_CLAN_ERROR);
        }

        receiverClan.addFriendClan(senderClan);
        clanRepository.saveAndFlush(receiverClan);
        clanToClanFriendRequestRepository
                .deleteBidirectionalClanRequest(clanToClanFriendRequest.getReceiverClan().getId(), clanToClanFriendRequest.getSenderClan().getId());

        return List.of(receiverClan.getId(), senderClan.getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Long> disapproveFriendClanRequest(Long requestId, Long userId) {
        ClanToClanFriendRequest clanToClanFriendRequest = clanToClanFriendRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(clanToClanFriendRequest.getClanToClanRequestStatus().equals(ClanToClanRequestStatus.REJECTED)) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }

        Clan receiverClan = clanToClanFriendRequest.getReceiverClan();

        ServerUser serverUser = receiverClan.getUserWithId(userId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        if(!receiverClan.isUserAdmin(userId)
                || serverUser.getClanPrivileges() == null
                || !serverUser.getClanPrivileges().isDiplomacyPrivileges())
        {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        clanToClanFriendRequest.setClanToClanRequestStatus(ClanToClanRequestStatus.REJECTED);
        clanToClanFriendRequestRepository.save(clanToClanFriendRequest);

        return List.of(receiverClan.getId(), clanToClanFriendRequest.getSenderClan().getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Long> removeFriendClan(Long clanId, Long friendCLanId, Long userId) {
        Clan clan = clanRepository.findById(clanId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        ServerUser serverUser = clan.getUserWithId(userId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        if(!clan.isUserAdmin(userId)
                || serverUser.getClanPrivileges() == null
                || !serverUser.getClanPrivileges().isDiplomacyPrivileges())
        {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        Clan friendClan = clanRepository.findById(friendCLanId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));


        clan.deleteFriendClan(friendClan);
        clanRepository.save(clan);

        return List.of(clan.getId(), friendClan.getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClanDTO changeUsersGrantPrivileges(Long memberUserId, List<ClanPrivilegesDTO> clanPrivilegesList, String serverId, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        Clan clan = serverUser.getClan();
        if(clan == null) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }
        ServerUser memberUser = clan.getUserWithId(memberUserId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(!memberUser.isClanAdmin()) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        for (ServerUser clanUser : clan.getUsers()) {
            if(clanUser.getUser_id().equals(userId) &&
                    clan.getUsers().stream().filter(ServerUser::isClanAdmin).toList().size() == 1) {
                continue;
            }

            clanPrivilegesList.stream()
                    .filter(userDto -> userDto.getUserId().equals(clanUser.getUser_id()))
                    .findFirst()
                    .ifPresent(clanUser::setClanPrivileges);
        }

        if(clan.getUsers().stream().filter(ServerUser::isClanAdmin).toList().size() == 0) {
            throw new CustomRunTimeException(ExceptionE.ADMIN_ERROR);
        }

        return clanMapper.toClanDTO(clanRepository.save(clan));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void changeGrantPrivileges(Long memberUserId, ClanPrivileges clanPrivileges, String serverId, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        Clan clan = serverUser.getClan();
        if(clan == null) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }
        ServerUser memberUser = clan.getUserWithId(memberUserId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        if(serverUser.getClanPrivileges() == null || !serverUser.getClanPrivileges().isAdminPrivileges() || memberUser.isClanAdmin()) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        if(clanPrivileges.getAdmin() && !memberUser.getCanUserCreateClan()) {
            throw new CustomRunTimeException(ExceptionE.EMBASSY_LVL_ADMIN_PRIVILEGES_ERROR);
        }

        ClanPrivileges dbCLanPrivileges = memberUser.getClanPrivileges();
        dbCLanPrivileges.setAdmin(clanPrivileges.getAdmin());
        dbCLanPrivileges.setDiplomacy(clanPrivileges.getDiplomacy());
        dbCLanPrivileges.setInvite(clanPrivileges.getInvite());
        dbCLanPrivileges.setSecretForum(clanPrivileges.getSecretForum());
        dbCLanPrivileges.setForumModerator(clanPrivileges.getForumModerator());
        dbCLanPrivileges.controlIsAdminAndMakeChanges();

        //memberUser.setClanPrivileges(dbCLanPrivileges);
        serverUserRepository.save(memberUser);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void reserveIsland(String serverId, String islandId, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        Clan clan = serverUser.getClan();
        if(clan == null) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }

        clan.addReserveIsland(serverUser, islandId);
        clanRepository.save(clan);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void unReserveIsland(String serverId, String islandId, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        Clan clan = serverUser.getClan();
        if(clan == null) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }

        ReserveIsland reserveIsland = clan.getReserveIsland(islandId);

        if(!serverUser.getClanPrivileges().isAdminPrivileges()
                && !reserveIsland.getServerUser().getUser_id().equals(userId)) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }
        clan.deleteReserveIsland(islandId);
        clanRepository.save(clan);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Boolean checkUsersSameClan(Long user1Id, Long user2Id, String serverId) {
        if(user1Id.equals(user2Id)) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }

        ServerUser serverUser1 = serverUserRepository.findByUserIdAndServerId(user1Id, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        ServerUser serverUser2 = serverUserRepository.findByUserIdAndServerId(user2Id, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        Clan serverUser1Clan = serverUser1.getClan();
        Clan serverUser2Clan = serverUser2.getClan();

        return serverUser1Clan != null && serverUser2Clan != null && serverUser1Clan.getId().equals(serverUser2Clan.getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public PublicClanDTO getClanPrivate(Long clanId) {
        Clan clan = clanRepository.findById(clanId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        PublicClanDTO publicClanDTO = new PublicClanDTO();
        publicClanDTO.setClanId(clan.getId());
        publicClanDTO.setClanDescription(clan.getClanDescription());
        publicClanDTO.setClanName(clan.getName());
        publicClanDTO.setClanShortName(clan.getShortName());
        publicClanDTO.setClanJoinTypeEnum(clan.getClanJoinTypeEnum());
        publicClanDTO.setAttachments(clan.getAttachments());
        publicClanDTO.setServerId(clan.getServer_id());
        publicClanDTO.setFounder(clan.getFounder() != null ? clan.getFounder().getUser() : null);
        return publicClanDTO;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<PublicClanDTO> getClans() {
        List<Clan> clans = clanRepository.findAll();

        return clans
                .stream()
                .map(clan -> {
                    PublicClanDTO publicClanDTO = new PublicClanDTO();
                    publicClanDTO.setClanId(clan.getId());
                    publicClanDTO.setClanDescription(clan.getClanDescription());
                    publicClanDTO.setClanName(clan.getName());
                    publicClanDTO.setClanShortName(clan.getShortName());
                    publicClanDTO.setClanJoinTypeEnum(clan.getClanJoinTypeEnum());
                    publicClanDTO.setAttachments(clan.getAttachments());
                    publicClanDTO.setServerId(clan.getServer_id());
                    publicClanDTO.setFounder(clan.getFounder() != null ? clan.getFounder().getUser() : null);
                    // publicClanDTO.setUserList(clan.getUsers().stream()
                    //         .map(serverUser -> new User(serverUser.getUser().getUserId(), serverUser.getUser().getUsername(), LocalDateTime.now()))
                    //         .collect(Collectors.toList()));
                    return publicClanDTO;
                }).toList();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<PublicUserDTO> getUsers() {
        List<ServerUser> serverUsers = serverUserRepository.findAll();
        return serverUsers
                .stream()
                .map(serverUser ->  {
                    Long clanId = serverUser.getClan() != null ? serverUser.getClan().getId() : null;
                    return new PublicUserDTO(serverUser.getUser().getUserId(), serverUser.getUser().getUsername(), clanId, serverUser.getServer_id());
                })
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public PublicUserDTO getUser(Long userId, String serverId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        Long clanId = serverUser.getClan() != null ? serverUser.getClan().getId() : null;
        return new PublicUserDTO(serverUser.getUser().getUserId(), serverUser.getUser().getUsername(), clanId, serverUser.getServer_id());
    }


}
