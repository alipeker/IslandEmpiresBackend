package com.islandempires.resourcesservice.dto.request;

import com.islandempires.resourcesservice.dto.RawMaterialsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidderAcceptorRawMaterialsDTO implements Serializable {
    private RawMaterialsDTO bidderRawMaterials;

    private RawMaterialsDTO acceptorRawMaterials;
}
