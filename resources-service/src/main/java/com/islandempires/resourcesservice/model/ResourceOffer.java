package com.islandempires.resourcesservice.model;

import com.islandempires.resourcesservice.dto.RawMaterialsDTO;
import com.islandempires.resourcesservice.enums.TransportResourceStatusEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceOffer implements Serializable {
    @Id
    @NotBlank
    private String id;

    @Indexed
    private String bidderIslandId;

    @Indexed
    private String acceptorIslandId;

    private LocalDateTime localDateTime;

    @Indexed
    private String serverId;

    @Indexed
    private String bidderResourceTransportId;

    @Indexed
    private String acceptorResourceTransportId;

    private RawMaterialsDTO bidderRawMaterials;

    private RawMaterialsDTO acceptorRawMaterials;

    private int bidderShipNumber;

    public ResourceOffer(String bidderIslandId, RawMaterialsDTO bidderRawMaterials, RawMaterialsDTO acceptorRawMaterials, int shipNumber,
                         LocalDateTime localDateTime, String serverId) {
        this.bidderIslandId = bidderIslandId;
        this.bidderRawMaterials = bidderRawMaterials;
        this.acceptorRawMaterials = acceptorRawMaterials;
        this.localDateTime = localDateTime;
        this.serverId = serverId;
        this.bidderShipNumber = shipNumber;
    }

}
