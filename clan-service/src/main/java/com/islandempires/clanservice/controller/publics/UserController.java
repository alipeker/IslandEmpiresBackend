package com.islandempires.clanservice.controller.publics;

import com.islandempires.clanservice.dto.UserDTO;
import com.islandempires.clanservice.dto.UserFriendDTO;
import com.islandempires.clanservice.dto.clan.ClanDTO;
import com.islandempires.clanservice.dto.user.UserPrivateDTO;
import com.islandempires.clanservice.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.islandempires.clanservice.model.ServerUser;

import java.util.List;

@RestController
@RequestMapping("clan/public/user")
@RequiredArgsConstructor
public class UserController {
    @NonNull
    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{serverId}")
    private UserPrivateDTO getServerUser(@PathVariable String serverId, @RequestAttribute("userId") Long userId) {
        return userService.getServerUserAll(userId, serverId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/sendFriendRequest/{serverId}/{receiverUserId}")
    private UserPrivateDTO sendFriendRequest(@PathVariable String serverId, @PathVariable Long receiverUserId, @RequestAttribute("userId") Long senderUserId) {
        userService.sendFriendRequest(serverId, receiverUserId, senderUserId);
        return userService.getServerUserAll(senderUserId, serverId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/removeFriendRequest/{requestId}")
    private UserPrivateDTO removeFriendRequest(@PathVariable Long requestId, @RequestAttribute("userId") Long userId) {
        String serverId = userService.removeFriendRequest(requestId, userId);
        return userService.getServerUserAll(userId, serverId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/removeFriend/{serverId}/{receiverUserId}")
    private UserPrivateDTO removeFriend(@PathVariable String serverId, @PathVariable Long receiverUserId, @RequestAttribute("userId") Long senderUserId) {
        userService.removeFriend(serverId, receiverUserId, senderUserId);
        return userService.getServerUserAll(senderUserId, serverId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/blockUser/{serverId}/{blockUserId}")
    private UserPrivateDTO blockUser(@PathVariable String serverId, @PathVariable Long blockUserId, @RequestAttribute("userId") Long userId) {
        userService.blockUser(serverId, blockUserId, userId);
        return userService.getServerUserAll(userId, serverId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/unBlockUser/{serverId}/{unBlockUserId}")
    private UserPrivateDTO unBlockUser(@PathVariable String serverId, @PathVariable Long unBlockUserId, @RequestAttribute("userId") Long userId) {
        userService.unBlockUser(serverId, unBlockUserId, userId);
        return userService.getServerUserAll(userId, serverId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/friends/{serverId}")
    private List<UserFriendDTO> getFriends(@PathVariable String serverId, @RequestAttribute("userId") Long userId) {
        return userService.getUserFriends(serverId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/rejectFriendRequest/{requestId}")
    public UserPrivateDTO rejectFriendRequest(@PathVariable Long requestId, @RequestAttribute("userId") Long userId) {
        String serverId = userService.rejectFriendRequest(requestId, userId);
        return userService.getServerUserAll(userId, serverId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/acceptFriendRequest/{requestId}")
    public UserPrivateDTO acceptFriendRequest(@PathVariable Long requestId, @RequestAttribute("userId") Long userId) {
        String serverId = userService.acceptFriendRequest(requestId, userId);
        return userService.getServerUserAll(userId, serverId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/pendingFriendsRequests/{serverId}")
    private List<UserFriendDTO> getPendingFriendRequestListToUser(@PathVariable String serverId, @RequestAttribute("userId") Long userId) {
        return userService.getPendingFriendRequestListToUser(serverId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/pendingFriendsMyRequests/{serverId}")
    private List<UserFriendDTO> getPendingFriendRequestListFromUser(@RequestAttribute("userId") Long userId, @PathVariable String serverId) {
        return userService.getPendingFriendRequestListFromUser(serverId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/rejectedFriendsRequests/{serverId}")
    private List<UserFriendDTO> getRejectedFriendRequestListToUser(@PathVariable String serverId, @RequestAttribute("userId") Long userId) {
        return userService.getRejectedFriendRequestListToUser(serverId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/rejectedFriendsMyRequests/{serverId}")
    private List<UserFriendDTO> getRejectedFriendRequestListFromUser(@PathVariable String serverId, @RequestAttribute("userId") Long userId) {
        return userService.getRejectedFriendRequestListFromUser(serverId, userId);
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getBlockList/{serverId}")
    private List<UserFriendDTO> getBlockList(@RequestAttribute("userId") Long userId, @PathVariable String serverId) {
        return userService.getBlockList(serverId, userId);
    }


}
