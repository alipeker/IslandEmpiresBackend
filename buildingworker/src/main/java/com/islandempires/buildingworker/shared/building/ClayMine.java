package com.islandempires.buildingworker.shared.building;

import com.islandempires.buildingworker.dto.IncreaseOrDecreaseIslandResourceFieldDTO;
import com.islandempires.buildingworker.enums.IslandResourceEnum;
import com.islandempires.buildingworker.shared.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingworker.shared.buildinglevelspec.ClayMineLevel;
import com.islandempires.buildingworker.shared.buildingtype.RawMaterialProductionStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import static com.islandempires.buildingworker.util.StaticVariables.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClayMine extends RawMaterialProductionStructures implements Serializable {
    private List<ClayMineLevel> clayMineLevelList;

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return clayMineLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }

    @Override
    public void islandBuildingLevelUpExecution(int nextLvl, String islandId) {
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        try {
            RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(closeableHttpClient));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", getServiceToken());

            IncreaseOrDecreaseIslandResourceFieldDTO increaseOrDecreaseIslandResourceFieldDTO = new IncreaseOrDecreaseIslandResourceFieldDTO();
            increaseOrDecreaseIslandResourceFieldDTO.setIslandResourceEnum(IslandResourceEnum.CLAY_HOURLY_PRODUCTION);

            int increaseOrDecreaseValue = (nextLvl > 1) ? clayMineLevelList.get(nextLvl - 1).getHourlyClayProduction() - clayMineLevelList.get(nextLvl - 2).getHourlyClayProduction() :
                                                          clayMineLevelList.get(nextLvl - 1).getHourlyClayProduction();


            increaseOrDecreaseIslandResourceFieldDTO.setValue(increaseOrDecreaseValue);

            HttpEntity<IncreaseOrDecreaseIslandResourceFieldDTO> requestEntity = new HttpEntity<>(increaseOrDecreaseIslandResourceFieldDTO, headers);

            try {
                restTemplate.exchange(getGatewayUrl() + "/resource/increaseOrDecreaseIslandResourceField/" + islandId, HttpMethod.PATCH, requestEntity, Object.class);
            } catch (Exception e) {
                throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } finally {
            try {
                closeableHttpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
