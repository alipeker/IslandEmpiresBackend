package com.islandempires.militaryservice.service;

import com.islandempires.militaryservice.converter.SoldierBaseInfoConverter;
import com.islandempires.militaryservice.dto.GameServerSoldierDTO;
import com.islandempires.militaryservice.dto.IslandResourceDTO;
import com.islandempires.militaryservice.dto.SoldierBaseInfoDTO;
import com.islandempires.militaryservice.enums.SoldierSubTypeEnum;
import com.islandempires.militaryservice.enums.SoldierTypeEnum;
import com.islandempires.militaryservice.model.GameServerSoldier;
import com.islandempires.militaryservice.model.resource.RawMaterialsAndPopulationCost;
import com.islandempires.militaryservice.model.soldier.ShipBaseInfo;
import com.islandempires.militaryservice.model.soldier.SoldierBaseInfo;
import com.islandempires.militaryservice.repository.GameServerSoldierBaseInfoRepository;
import com.islandempires.militaryservice.service.client.MilitaryGatewayClient;
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
import java.util.stream.Collectors;

@Service
public class GameServerPropertiesService {

    @Autowired
    private GameServerSoldierBaseInfoRepository gameServerSoldierBaseInfoRepository;

    @Autowired
    private SoldierBaseInfoConverter soldierBaseInfoConverter;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MilitaryGatewayClient militaryGatewayClient;

    @Value("${admin-token}")
    private String adminToken;


    @PostConstruct
    public void getAllBuildingsServerProperties() {

        List<GameServerSoldierDTO> serverSoldierProperties = militaryGatewayClient.getGameServerSoldierProperties();

        serverSoldierProperties.forEach(gameServerSoldierProperty -> {
            Optional<GameServerSoldier> gameServerSoldierOptional = gameServerSoldierBaseInfoRepository.findById(gameServerSoldierProperty.getId());

            if(gameServerSoldierOptional.isPresent()) {
                GameServerSoldier gameServerSoldier = gameServerSoldierOptional.get();
                gameServerSoldier.getSoldierBaseInfoList().forEach(soldierBaseInfo -> {
                    SoldierBaseInfoDTO updatedSoldierBaseInfoDTO = gameServerSoldierProperty.getSoldierBaseInfoList().stream()
                            .filter(soldierBaseInfoDTO -> soldierBaseInfoDTO.getId().equals(soldierBaseInfo.getSoldierSubTypeName())).findFirst().orElseThrow();

                    soldierBaseInfo.setAttackPoint(updatedSoldierBaseInfoDTO.getAttackPoint());
                    soldierBaseInfo.setDefensePoints(updatedSoldierBaseInfoDTO.getDefensePoints());
                    soldierBaseInfo.setProductionDuration(updatedSoldierBaseInfoDTO.getProductionDuration());
                    soldierBaseInfo.updateRawMaterialAndPopulationCost(updatedSoldierBaseInfoDTO.getRawMaterialsAndPopulationCost());
                });
                gameServerSoldier.getShipBaseInfoList().forEach(soldierBaseInfo -> {
                    SoldierBaseInfoDTO updatedSoldierBaseInfoDTO = gameServerSoldierProperty.getSoldierBaseInfoList().stream()
                            .filter(soldierBaseInfoDTO -> soldierBaseInfoDTO.getId().equals(soldierBaseInfo.getShipSubTypeName())).findFirst().orElseThrow();

                    soldierBaseInfo.setTimeToTraverseMapCell(updatedSoldierBaseInfoDTO.getTimeToTraverseMapCell());
                    soldierBaseInfo.setCanonCapacityOfShip(updatedSoldierBaseInfoDTO.getCanonCapacityOfShip());
                    soldierBaseInfo.setSoldierCapacityOfShip(updatedSoldierBaseInfoDTO.getSoldierCapacityOfShip());
                    soldierBaseInfo.setTotalLootCapacity(updatedSoldierBaseInfoDTO.getTotalLootCapacity());
                });
                gameServerSoldierBaseInfoRepository.save(gameServerSoldier);
            } else {
                GameServerSoldier newGameServerSoldier = new GameServerSoldier(gameServerSoldierProperty.getId());
                List<SoldierBaseInfo> soldierBaseInfoList = soldierBaseInfoConverter.convertToEntityList(gameServerSoldierProperty.getSoldierBaseInfoList(), newGameServerSoldier);
                List<ShipBaseInfo> shipBaseInfoList = soldierBaseInfoConverter.convertToShipEntityList(
                        gameServerSoldierProperty.getSoldierBaseInfoList().stream().filter(soldierBaseInfo ->
                                    soldierBaseInfo.getId().equals(SoldierSubTypeEnum.HOLK.toString()) ||
                                    soldierBaseInfo.getId().equals(SoldierSubTypeEnum.GUN_HOLK.toString()) ||
                                    soldierBaseInfo.getId().equals(SoldierSubTypeEnum.CARRACK.toString())
                        ).collect(Collectors.toList()), newGameServerSoldier);
                newGameServerSoldier.setSoldierBaseInfoList(soldierBaseInfoList);
                newGameServerSoldier.setShipBaseInfoList(shipBaseInfoList);
                gameServerSoldierBaseInfoRepository.save(newGameServerSoldier);
            }

        });
    }

}
