package com.islandempires.buildingworker.model.building;

import com.islandempires.buildingworker.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingworker.model.buildinglevelspec.TimberCampLevel;
import com.islandempires.buildingworker.model.buildingtype.RawMaterialProductionStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimberCamp extends RawMaterialProductionStructures implements Serializable {
    private List<TimberCampLevel> timberCampLevelList;

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return timberCampLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }

    @Override
    public void executeBuildingLogic() {
        restTemplate.exchange("https://www.google.com", HttpMethod.GET, null, String.class);
    }
}

