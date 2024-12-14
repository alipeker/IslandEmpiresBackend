package com.islandempires.buildingservice.shared.building;

import com.islandempires.buildingservice.shared.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.shared.buildinglevelspec.CommercialPortLevel;
import com.islandempires.buildingservice.shared.buildingtype.TradeStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommercialPort extends TradeStructures {
    private List<CommercialPortLevel> commercialPortLevelList;

    @Override
    public List<BuildingLevel> getBuildingLevelList() {
        return commercialPortLevelList.stream().map(BuildingLevel.class::cast).collect(Collectors.toList());
    }
}
