package com.islandempires.resourcesservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateIslandTradingDTO implements Serializable {
    int totalShipNumber;

    long shipCapacity;

    int timeReductionPercentage;
}
