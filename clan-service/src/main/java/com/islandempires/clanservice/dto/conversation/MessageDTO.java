package com.islandempires.clanservice.dto.conversation;

import com.islandempires.clanservice.dto.clan.ClanUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO implements Serializable {
    private Long id;

    private ClanUserDTO sender;

    private String body;

    private LocalDateTime timestamp;
}
