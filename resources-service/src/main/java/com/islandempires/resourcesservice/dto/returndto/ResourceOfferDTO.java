package com.islandempires.resourcesservice.dto.returndto;

import com.islandempires.resourcesservice.model.ResourceOffer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceOfferDTO implements Serializable {
    private ResourceOffer resourceOffer;

    private int pageNumber;
}
