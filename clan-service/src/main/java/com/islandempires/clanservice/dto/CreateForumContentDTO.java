package com.islandempires.clanservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateForumContentDTO {
    private String header;

    private ForumMessageDTO forumMessage;
}
