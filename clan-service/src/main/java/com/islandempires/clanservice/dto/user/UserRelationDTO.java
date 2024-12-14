package com.islandempires.clanservice.dto.user;

import com.islandempires.clanservice.enums.FriendRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRelationDTO implements Serializable {

    private Long id;

    private Long senderId;

    private String senderUsername;

    private Long receiverId;

    private String receiverUsername;

    private FriendRequestStatus friendRequestStatus;

    private LocalDateTime timestamp;

}
