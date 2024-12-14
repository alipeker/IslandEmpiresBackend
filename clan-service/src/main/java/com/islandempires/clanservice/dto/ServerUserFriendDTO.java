package com.islandempires.clanservice.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.islandempires.clanservice.enums.FriendRequestStatus;
import com.islandempires.clanservice.model.ServerUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerUserFriendDTO {
    private Long id;

    private List<ServerUser> friends = new ArrayList<>();

}
