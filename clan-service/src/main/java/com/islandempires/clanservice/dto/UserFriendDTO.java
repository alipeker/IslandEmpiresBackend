package com.islandempires.clanservice.dto;

import com.islandempires.clanservice.enums.FriendRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFriendDTO implements Serializable {
    private Long id;
    private String username;
    private FriendRequestStatus friendStatus;
}
