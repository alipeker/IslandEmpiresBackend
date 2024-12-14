package com.islandempires.clanservice.service.gameServer;

import com.islandempires.clanservice.dto.GameServerResponseDTO;
import com.islandempires.clanservice.filter.client.WhoAmIClient;
import com.islandempires.clanservice.model.Server;
import com.islandempires.clanservice.repository.ServerRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GameServerPropertiesService {

    private final WhoAmIClient gatewayClient;

    private final ServerRepository serverRepository;

    @PostConstruct
    private void getGameServers() {
        List<GameServerResponseDTO> gameServerResponseDTOList = gatewayClient.getGameServerSoldierProperties();

        if(gameServerResponseDTOList.size() > 0) {
            gameServerResponseDTOList.forEach(gameServerResponseDTO -> {
                Optional<Server> serverOptional = serverRepository.findById(gameServerResponseDTO.getId());
                if(serverOptional.isEmpty()) {
                    Server newServer = new Server();
                    newServer.setId(gameServerResponseDTO.getId());
                    serverRepository.save(newServer);
                }
            });
        }
    }
}
