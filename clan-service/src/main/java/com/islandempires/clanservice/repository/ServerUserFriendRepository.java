package com.islandempires.clanservice.repository;

import com.islandempires.clanservice.dto.UserFriendDTO;
import com.islandempires.clanservice.enums.FriendRequestStatus;
import com.islandempires.clanservice.model.ServerUser;
import com.islandempires.clanservice.model.ServerUserFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServerUserFriendRepository extends JpaRepository<ServerUserFriend, Long> {

    @Query("SELECT suf FROM ServerUserFriend suf WHERE suf.friendRequestStatus = 'PENDING' AND suf.receiverUser = :receiverUser")
    List<ServerUserFriend> findFriendRequestsToUser(@Param("receiverUser") ServerUser receiverUser);

    @Query("SELECT new com.islandempires.clanservice.dto.UserFriendDTO(u.id, u.username, f.friendRequestStatus) " +
            "FROM ServerUserFriend f " +
            "JOIN f.senderUser su " +
            "JOIN su.user u " +
            "WHERE f.senderUser.id = :userId " +
            "AND f.friendRequestStatus = :status")
    List<UserFriendDTO> findFriendsFromUserByStatus(@Param("userId") Long userId, @Param("status") FriendRequestStatus status);

    @Query("SELECT new com.islandempires.clanservice.dto.UserFriendDTO(u.id, u.username, f.friendRequestStatus) " +
            "FROM ServerUserFriend f " +
            "JOIN f.receiverUser su " +
            "JOIN su.user u " +
            "WHERE f.receiverUser.id = :userId " +
            "AND f.friendRequestStatus = :status")
    List<UserFriendDTO> findFriendsToUserByStatus(@Param("userId") Long userId, @Param("status") FriendRequestStatus status);

    @Query("SELECT new com.islandempires.clanservice.dto.UserFriendDTO(u.id, u.username, f.friendRequestStatus) " +
            "FROM ServerUserFriend f " +
            "JOIN f.senderUser su " +
            "JOIN su.user u " +
            "WHERE (f.senderUser.id = :userId OR f.receiverUser.id = :userId) " +
            "AND f.friendRequestStatus = :status")
    List<UserFriendDTO> findAllFriendList(@Param("userId") Long userId, @Param("status") FriendRequestStatus status);

    @Query("SELECT s FROM ServerUserFriend s WHERE " +
            "((s.senderUser.id = :user1 AND s.receiverUser.id = :user2) OR " +
            "(s.senderUser.id = :user2 AND s.receiverUser.id = :user1))")
    Optional<ServerUserFriend> findFriendShipBetweenUsers(@Param("user1") Long user1,
                                                          @Param("user2") Long user2);

    @Transactional
    @Modifying
    @Query("DELETE FROM ServerUserFriend s WHERE " +
            "((s.senderUser.id = :user1 AND s.receiverUser.id = :user2) OR " +
            "(s.senderUser.id = :user2 AND s.receiverUser.id = :user1))")
    void deleteFriendshipBetweenUsers(@Param("user1") Long user1,
                                      @Param("user2") Long user2);
}
