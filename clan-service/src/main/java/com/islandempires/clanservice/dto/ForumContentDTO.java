package com.islandempires.clanservice.dto;

import com.islandempires.clanservice.model.Forum;
import com.islandempires.clanservice.model.ForumMessage;
import com.islandempires.clanservice.model.ServerUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumContentDTO {

    private Long id;

    private List<ForumMessageDTO> forumMessageList = new ArrayList<>();

    private ServerUserDTO serverUser;

    private String header;

    private ForumMessageDTO forumMessage;

    private String localDateTime;

    private String updatedLocalDateTime;
}

