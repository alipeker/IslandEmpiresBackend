package com.islandempires.resourcesservice.dto.request;


import com.islandempires.resourcesservice.dto.RawMaterialsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MutualTradingRequestDTO implements Serializable {
    private RawMaterialsDTO island1MaterialsDTO;

    private RawMaterialsDTO island2MaterialsDTO;
}
