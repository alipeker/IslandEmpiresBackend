package com.islandempires.clanservice.dto.conversation;

import com.islandempires.clanservice.dto.clan.ClanUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDTO implements Serializable {

    private Long id;

    private List<ClanUserDTO> participants = new ArrayList<>();

    private List<MessageDTO> messages = new ArrayList<>();

    private ClanUserDTO creatorServerUser;

    private LocalDateTime localDateTime;

    private LocalDateTime lastMessageLocalDateTime;
}
