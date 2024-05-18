package com.islandempires.buildingworker.model.building;

import com.islandempires.buildingworker.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingworker.model.buildinglevelspec.DairyFarmLevel;
import com.islandempires.buildingworker.model.buildingtype.FoodProductionStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DairyFarm extends FoodProductionStructures implements Serializable {
    private List<DairyFarmLevel> dairyFarmLevelList;

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return dairyFarmLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }

    @Override
    public void executeBuildingLogic() {

    }
}
