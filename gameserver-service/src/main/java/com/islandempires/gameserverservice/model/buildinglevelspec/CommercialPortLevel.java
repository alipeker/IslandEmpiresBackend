package com.islandempires.gameserverservice.model.buildinglevelspec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommercialPortLevel extends BuildingLevel {

    private int cargoShipNumber;

    private int cargoShipCapacity;

    private int timeReductionPercentage;

}
