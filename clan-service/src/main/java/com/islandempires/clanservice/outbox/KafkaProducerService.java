package com.islandempires.clanservice.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.islandempires.clanservice.dto.UserDTO;
import com.islandempires.clanservice.dto.clan.ClanDTO;
import com.islandempires.clanservice.dto.user.UserPrivateDTO;
import com.islandempires.clanservice.exception.CustomRunTimeException;
import com.islandempires.clanservice.exception.ExceptionE;
import com.islandempires.clanservice.mapper.ClanMapper;
import com.islandempires.clanservice.mapper.UserMapper;
import com.islandempires.clanservice.model.Clan;
import com.islandempires.clanservice.model.ServerUser;
import com.islandempires.clanservice.service.ClanService;
import com.islandempires.clanservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.islandempires.clanservice.dto.kafka.ClanDataMessage;

import com.islandempires.clanservice.dto.KafkaClanUserDTO;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    @Value("${spring.kafka.producer.clan-change-topic}")
    private String clanTopicName;

     @Value("${spring.kafka.producer.clan-delete-topic}")
    private String clanDeleteTopicName;

    @Value("${spring.kafka.producer.clan-user-change-topic}")
    private String clanUserTopicName;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendClan(Long clanId) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String clanJson = objectMapper.writeValueAsString(clanId);
            this.kafkaTemplate.send(clanTopicName, clanJson);
        } catch (Exception e) {

        }
    }

    public void deleteClan(Long clanId) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String clanJson = objectMapper.writeValueAsString(clanId);
            this.kafkaTemplate.send(clanDeleteTopicName, clanJson);
        } catch (Exception e) {

        }
    }

    public void sendServerUser(KafkaClanUserDTO kafkaClanUserDTO) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String clanUserJson = objectMapper.writeValueAsString(kafkaClanUserDTO);
            this.kafkaTemplate.send(clanUserTopicName, clanUserJson);
        } catch (Exception e) {

        }
    }
}
