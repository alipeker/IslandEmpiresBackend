package com.islandempires.buildingworker.model.building;

import com.islandempires.buildingworker.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingworker.model.buildinglevelspec.MillLevel;
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
public class Mill extends FoodProductionStructures implements Serializable {
    private List<MillLevel> millLevelList;


    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return millLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }

    @Override
    public void executeBuildingLogic() {

    }
}
