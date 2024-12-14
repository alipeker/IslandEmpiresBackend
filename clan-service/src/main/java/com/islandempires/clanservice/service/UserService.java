package com.islandempires.clanservice.service;

import com.islandempires.clanservice.dto.UserDTO;
import com.islandempires.clanservice.dto.UserFriendDTO;
import com.islandempires.clanservice.dto.UserRegisterDTO;
import com.islandempires.clanservice.dto.clan.ClanDTO;
import com.islandempires.clanservice.dto.user.UserPrivateDTO;
import com.islandempires.clanservice.enums.FriendRequestStatus;
import com.islandempires.clanservice.exception.CustomRunTimeException;
import com.islandempires.clanservice.exception.ExceptionE;
import com.islandempires.clanservice.filter.client.WhoAmIClient;
import com.islandempires.clanservice.mapper.ClanMapper;
import com.islandempires.clanservice.mapper.UserMapper;
import com.islandempires.clanservice.model.ServerUser;
import com.islandempires.clanservice.model.ServerUserFriend;
import com.islandempires.clanservice.model.User;
import com.islandempires.clanservice.outbox.KafkaProducerService;
import com.islandempires.clanservice.repository.ServerUserFriendRepository;
import com.islandempires.clanservice.repository.ServerUserRepository;
import com.islandempires.clanservice.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final ServerUserRepository serverUserRepository;

    @NonNull
    private final ServerUserFriendRepository serverUserFriendRepository;

    @NonNull
    private final WhoAmIClient whoAmIClient;

    @NonNull
    private final ClanMapper clanMapper;

    @NonNull
    private final UserMapper userMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registerUser(UserRegisterDTO userRegisterDTO) {
        UserDTO userDTO;
        try {
            userDTO = whoAmIClient.getUser(userRegisterDTO.getId());
        } catch (Exception e) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }
        userRegisterDTO.setUsername(userDTO.getUsername());

        Optional<User> optionalUser = userRepository.findByUserId(userRegisterDTO.getId());
        User user = optionalUser.orElseGet(() ->  new User(userRegisterDTO.getId(), userRegisterDTO.getUsername(), LocalDateTime.now()));
        user.addUserToServerList(userRegisterDTO.getServerId());
        ServerUser serverUser = user.getUserServerList().stream()
                .filter(serverUserf -> serverUserf.getServer_id().equals(userRegisterDTO.getServerId())).findFirst()
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        serverUser.initializeGrantPrivileges();
        userRepository.save(user);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ServerUserFriend sendFriendRequest(String serverId, Long receiverUserId, Long senderUserId) {
        ServerUser senderServerUser = getServerUser(senderUserId, serverId);

        if(senderServerUser.getFriendList().size() >= 100) {
            throw new CustomRunTimeException(ExceptionE.MAX_FRIEND_ERROR);
        }

        ServerUser receiverServerUser = getServerUser(receiverUserId, serverId);

        if(receiverServerUser.controlIsBlocked(senderServerUser.getUser_id())) {
            throw new CustomRunTimeException(ExceptionE.BLOCKED);
        }

        if(senderServerUser.controlIsBlocked(receiverServerUser.getId())) {
            throw new CustomRunTimeException(ExceptionE.BLOCKED_FROM_YOU);
        }

        Optional<ServerUserFriend> serverUserFriendOptional =
                serverUserFriendRepository.findFriendShipBetweenUsers(senderServerUser.getId(), receiverServerUser.getId());

        if (serverUserFriendOptional.isPresent()) {
            return handleExistingFriendRequest(serverUserFriendOptional.get(), senderServerUser, receiverServerUser);
        }

        return createNewFriendRequest(senderServerUser, receiverServerUser);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    private ServerUser getServerUser(Long userId, String serverId) {
        return serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public UserPrivateDTO getServerUserAll(Long userId, String serverId) {
        entityManager.clear();
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        return userMapper.toUserDTO(serverUser);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private ServerUserFriend handleExistingFriendRequest(ServerUserFriend serverUserFriend,
                                                         ServerUser senderServerUser,
                                                         ServerUser receiverServerUser) {
        boolean isPending = serverUserFriend.getFriendRequestStatus().equals(FriendRequestStatus.PENDING);
        boolean isRejected = serverUserFriend.getFriendRequestStatus().equals(FriendRequestStatus.REJECTED);
        boolean isReceiverAlreadyRequested = serverUserFriend.getReceiverUser_id().equals(senderServerUser.getId());

        if (isReceiverAlreadyRequested) {
            if (isPending) {
                return acceptFriendRequest(serverUserFriend);
            } else if (isRejected) {
                return resendFriendRequest(serverUserFriend, senderServerUser, receiverServerUser);
            }
        }

        throw new CustomRunTimeException(ExceptionE.ALREADY_EXIST);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private ServerUserFriend acceptFriendRequest(ServerUserFriend serverUserFriend) {
        serverUserFriend.acceptFriendRequest();
        serverUserFriend.setTimestamp(LocalDateTime.now());
        return serverUserFriendRepository.save(serverUserFriend);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private ServerUserFriend resendFriendRequest(ServerUserFriend serverUserFriend,
                                                 ServerUser senderServerUser,
                                                 ServerUser receiverServerUser) {
        serverUserFriend.setReceiverUser_id(receiverServerUser.getId());
        serverUserFriend.setSenderUser_id(senderServerUser.getId());
        serverUserFriend.pendingFriendRequest();
        serverUserFriend.setTimestamp(LocalDateTime.now());
        return serverUserFriendRepository.save(serverUserFriend);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private ServerUserFriend createNewFriendRequest(ServerUser senderServerUser, ServerUser receiverServerUser) {
        ServerUserFriend serverUserFriend = new ServerUserFriend(senderServerUser.getId(), receiverServerUser.getId(),
                FriendRequestStatus.PENDING, LocalDateTime.now());
        return serverUserFriendRepository.save(serverUserFriend);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String removeFriendRequest(Long requestId, Long userId) {
        ServerUserFriend serverUserFriend =
                serverUserFriendRepository.findById(requestId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(!serverUserFriend.getSenderUser().getUser_id().equals(userId)) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        serverUserFriendRepository.deleteById(serverUserFriend.getId());

        return serverUserFriend.getSenderUser().getServer_id();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removeFriend(String serverId, Long receiverUserId, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        ServerUser friendServerUser = serverUserRepository.findByUserIdAndServerId(receiverUserId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        serverUserFriendRepository.deleteFriendshipBetweenUsers(serverUser.getId(), friendServerUser.getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void blockUser(String serverId, Long blockUserId, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        ServerUser blockServerUser = serverUserRepository.findByUserIdAndServerId(blockUserId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        serverUserFriendRepository.deleteFriendshipBetweenUsers(serverUser.getId(), blockServerUser.getId());
        serverUser.addBlockList(blockServerUser);
        serverUserRepository.save(serverUser);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void unBlockUser(String serverId, Long unBlockUserId, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        ServerUser unblockServerUser = serverUserRepository.findByUserIdAndServerId(unBlockUserId, serverId)
                                            .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        serverUser.removeBlockList(unblockServerUser.getId());
        serverUserRepository.save(serverUser);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String rejectFriendRequest(Long requestId, Long userId) {
        ServerUserFriend serverUserFriend =
                serverUserFriendRepository.findById(requestId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(!serverUserFriend.getReceiverUser().getUser_id().equals(userId)) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        serverUserFriend.setFriendRequestStatus(FriendRequestStatus.REJECTED);
        serverUserFriendRepository.save(serverUserFriend);

        return serverUserFriend.getSenderUser().getServer_id();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String acceptFriendRequest(Long requestId, Long userId) {
        ServerUserFriend serverUserFriend =
                serverUserFriendRepository.findById(requestId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(serverUserFriend.getReceiverUser().getFriendList().size() >= 100) {
            throw new CustomRunTimeException(ExceptionE.MAX_FRIEND_ERROR);
        }

        if(!serverUserFriend.getReceiverUser().getUser_id().equals(userId)) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        serverUserFriend.setFriendRequestStatus(FriendRequestStatus.ACCEPTED);
        serverUserFriendRepository.save(serverUserFriend);

        return serverUserFriend.getSenderUser().getServer_id();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<UserFriendDTO> getPendingFriendRequestListFromUser(String serverId, Long userId) {
        ServerUser userServer = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        return serverUserFriendRepository.findFriendsToUserByStatus(userServer.getId(), FriendRequestStatus.ACCEPTED);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<UserFriendDTO> getUserFriends(String serverId, Long userId) {
        ServerUser userServer = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        return serverUserFriendRepository.findAllFriendList(userServer.getId(), FriendRequestStatus.ACCEPTED);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<UserFriendDTO> getPendingFriendRequestListToUser(String serverId, Long userId) {
        ServerUser userServer = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        return serverUserFriendRepository.findFriendsToUserByStatus(userServer.getId(), FriendRequestStatus.PENDING);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<UserFriendDTO> getRejectedFriendRequestListToUser(String serverId, Long userId) {
        ServerUser userServer = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        return serverUserFriendRepository.findFriendsToUserByStatus(userServer.getId(), FriendRequestStatus.REJECTED);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<UserFriendDTO> getRejectedFriendRequestListFromUser(String serverId, Long userId) {
        ServerUser userServer = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        return serverUserFriendRepository.findFriendsFromUserByStatus(userServer.getId(), FriendRequestStatus.REJECTED);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private UserFriendDTO convertToDTO(ServerUser serverUser) {
        Hibernate.initialize(serverUser.getUser());
        User user = serverUser.getUser();
        return new UserFriendDTO(user.getUserId(), user.getUsername(), FriendRequestStatus.PENDING);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<UserFriendDTO> getBlockList(String serverId, Long userId) {
        return serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND))
                .getBlockList()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void joinClanAllowed(String serverId, Long userId) {
         ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
         serverUser.setCanUserJoinClan(true);
         serverUserRepository.save(serverUser);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createClanAllowed(String serverId, Long userId, int maxClanMemberNumber) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        serverUser.setCanUserCreateClan(true);
        serverUser.setMaxClanMemberNumber(maxClanMemberNumber);
        serverUserRepository.save(serverUser);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setClanMaxMemberNumber(String serverId, Long userId, int maxClanMemberNumber) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        serverUser.setMaxClanMemberNumber(maxClanMemberNumber);
        serverUserRepository.save(serverUser);
    }


}
