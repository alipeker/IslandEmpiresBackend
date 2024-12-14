package com.islandempires.clanservice.mapper;

import com.islandempires.clanservice.dto.clan.ClanPrivilegesDTO;
import com.islandempires.clanservice.dto.clan.ClanUserDTO;
import com.islandempires.clanservice.dto.conversation.ConversationDTO;
import com.islandempires.clanservice.dto.conversation.MessageDTO;
import com.islandempires.clanservice.model.Conversation;
import com.islandempires.clanservice.model.Message;
import com.islandempires.clanservice.model.ServerUser;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ConversationMapper {
    private Long id;

    private List<ClanUserDTO> participants = new ArrayList<>();

    private List<MessageDTO> messages = new ArrayList<>();

    private ClanUserDTO creatorServerUser;

    private LocalDateTime localDateTime;

    private LocalDateTime lastMessageLocalDateTime;
    public ConversationDTO toConversationDTO(Conversation conversation) {
        return new ConversationDTO(conversation.getId(),
                conversation.getParticipants()
                        .stream()
                        .map(this::toClanUserDTO)
                        .collect(Collectors.toList()),
                conversation.getMessages()
                        .stream()
                        .map(this::toMessageDTO)
                        .collect(Collectors.toList()),
                toClanUserDTO(conversation.getCreatorServerUser()),
                conversation.getLocalDateTime(),
                conversation.getLastMessageLocalDateTime());
    }

    public MessageDTO toMessageDTO(Message message) {
        return new MessageDTO(message.getId(), toClanUserDTO(message.getSender()), message.getBody(), message.getTimestamp());
    }

    public ClanUserDTO toClanUserDTO(ServerUser serverUser) {
        return new ClanUserDTO(
                        serverUser.getUser_id(),
                        serverUser.getUser().getUsername(),
                        serverUser.getClanPrivileges() != null ? new ClanPrivilegesDTO(serverUser.getClanPrivileges().getId(),
                                serverUser.getClanPrivileges().getAdmin(),
                                serverUser.getClanPrivileges().getInvite(),
                                serverUser.getClanPrivileges().getDiplomacy(),
                                serverUser.getClanPrivileges().getForumModerator(),
                                serverUser.getClanPrivileges().getSecretForum()) : null
                );
    }

}
