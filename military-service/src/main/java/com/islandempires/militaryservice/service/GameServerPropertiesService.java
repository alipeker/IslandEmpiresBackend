package com.islandempires.militaryservice.service;

import com.islandempires.militaryservice.converter.SoldierBaseInfoConverter;
import com.islandempires.militaryservice.dto.GameServerSoldierDTO;
import com.islandempires.militaryservice.dto.SoldierBaseInfoDTO;
import com.islandempires.militaryservice.model.GameServerSoldier;
import com.islandempires.militaryservice.model.soldier.SoldierBaseInfo;
import com.islandempires.militaryservice.repository.GameServerSoldierBaseInfoRepository;
import jakarta.annotation.PostConstruct;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class GameServerPropertiesService {

    @Autowired
    private GameServerSoldierBaseInfoRepository gameServerSoldierBaseInfoRepository;

    @Autowired
    private SoldierBaseInfoConverter soldierBaseInfoConverter;

    @Autowired
    private ModelMapper modelMapper;

    private final WebClient islandResourceWebClient;

    @Autowired
    public GameServerPropertiesService(@Qualifier("gatewayClient") WebClient.Builder webClientBuilder,
                            @Value("${urls.gateway}") String gatewayUrl) {
        System.out.println(gatewayUrl);
        this.islandResourceWebClient = webClientBuilder
                .baseUrl(gatewayUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }

    @PostConstruct
    public void getAllBuildingsServerProperties() {

        Flux<GameServerSoldierDTO> serverSoldierProperties = getGameServerSoldierProperties();

        getGameServerSoldierProperties().subscribe(gameServerSoldierProperty -> {
            Optional<GameServerSoldier> gameServerSoldierOptional = gameServerSoldierBaseInfoRepository.findById(gameServerSoldierProperty.getId());
            //List<SoldierBaseInfo> soldierBaseInfoList = soldierBaseInfoConverter.convertToEntityList(gameServerSoldierProperty.getSoldierBaseInfoList(), newGameServerSoldier);

            if(gameServerSoldierOptional.isPresent()) {
                GameServerSoldier gameServerSoldier = gameServerSoldierOptional.get();
                gameServerSoldier.getSoldierBaseInfoList().forEach(soldierBaseInfo -> {
                    SoldierBaseInfoDTO updatedSoldierBaseInfoDTO = gameServerSoldierProperty.getSoldierBaseInfoList().stream()
                            .filter(soldierBaseInfoDTO -> soldierBaseInfoDTO.getId().equals(soldierBaseInfo.getSoldierSubTypeName())).findFirst().orElseThrow();

                    soldierBaseInfo.setAttackPoint(updatedSoldierBaseInfoDTO.getAttackPoint());
                    soldierBaseInfo.setDefensePoints(updatedSoldierBaseInfoDTO.getDefensePoints());
                    soldierBaseInfo.setRawMaterialsAndPopulationCost(updatedSoldierBaseInfoDTO.getRawMaterialsAndPopulationCost());
                });
                gameServerSoldierBaseInfoRepository.save(gameServerSoldier);
            } else {
                GameServerSoldier newGameServerSoldier = new GameServerSoldier(gameServerSoldierProperty.getId());
                List<SoldierBaseInfo> soldierBaseInfoList = soldierBaseInfoConverter.convertToEntityList(gameServerSoldierProperty.getSoldierBaseInfoList(), newGameServerSoldier);
                newGameServerSoldier.setSoldierBaseInfoList(soldierBaseInfoList);
                gameServerSoldierBaseInfoRepository.save(newGameServerSoldier);
            }

        });
    }


    public Flux<GameServerSoldierDTO> getGameServerSoldierProperties() {
        return islandResourceWebClient.get()
                .uri("/gameservice/getGameServerSoldierProperties")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(GameServerSoldierDTO.class)
                .onErrorResume(e -> {
                    return Flux.error(e);
                });
    }
}
