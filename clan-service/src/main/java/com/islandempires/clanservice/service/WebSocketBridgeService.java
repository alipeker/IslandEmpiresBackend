package com.islandempires.clanservice.service;

import com.islandempires.clanservice.dto.UserDTO;
import com.islandempires.clanservice.dto.UserFriendDTO;
import com.islandempires.clanservice.dto.UserRegisterDTO;
import com.islandempires.clanservice.dto.clan.ClanDTO;
import com.islandempires.clanservice.dto.user.UserPrivateDTO;
import com.islandempires.clanservice.enums.FriendRequestStatus;
import com.islandempires.clanservice.exception.CustomRunTimeException;
import com.islandempires.clanservice.exception.ExceptionE;
import com.islandempires.clanservice.filter.client.WhoAmIClient;
import com.islandempires.clanservice.outbox.KafkaProducerService;
import com.islandempires.clanservice.repository.ServerUserFriendRepository;
import com.islandempires.clanservice.repository.ServerUserRepository;
import com.islandempires.clanservice.repository.UserRepository;
import com.islandempires.clanservice.outbox.KafkaProducerService;
import com.islandempires.clanservice.dto.KafkaClanUserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.HashMap;

@Service
@AllArgsConstructor
public class WebSocketBridgeService {

    private final WhoAmIClient whoAmIClient;

    private final KafkaProducerService kafkaProducerService;

    public void sendClanChange(Long clanId, ClanDTO clanDTO) {
        try {
            whoAmIClient.sendClanChange(clanId, clanDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendClansChange(HashMap<Long, ClanDTO> clanDTO) {
        try {
            whoAmIClient.sendClansChange(clanDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendUserChange(String serverId, Long userId, UserPrivateDTO userPrivateDTO) {
        try {
            whoAmIClient.sendClanUserChange(serverId, userId, userPrivateDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendDeleteClanEvent(Long clanId) {
        try {
            whoAmIClient.sendDeleteClanEvent(clanId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendClanUserChange(String serverId, Long userId, UserPrivateDTO userPrivateDTO) {
        try {
            whoAmIClient.sendClanUserChange(serverId, userId, userPrivateDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publishClanChange(Long clanId) {
        kafkaProducerService.sendClan(clanId);
    }

    public void publishClanUserChange(String serverId, Long userId) {
        KafkaClanUserDTO kafkaClanUserDTO = new KafkaClanUserDTO(userId, serverId);
        kafkaProducerService.sendServerUser(kafkaClanUserDTO);
    }

    public void deleteClanChange(Long clanId) {
        kafkaProducerService.deleteClan(clanId);
    }
}
