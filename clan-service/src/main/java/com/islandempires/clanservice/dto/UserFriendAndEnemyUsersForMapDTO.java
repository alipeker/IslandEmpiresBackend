package com.islandempires.clanservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFriendAndEnemyUsersForMapDTO implements Serializable {

    private HashSet<Long> userFriends = new HashSet<>();

    private HashSet<Long> userBlocks = new HashSet<>();

    private HashSet<Long> userClanMembers = new HashSet<>();

    private HashSet<Long> userClanEnemyUsers = new HashSet<>();

    private HashSet<Long> userClanFriendUsers = new HashSet<>();
}
