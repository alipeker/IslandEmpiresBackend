package com.islandempires.resourcesservice.model;

import com.islandempires.resourcesservice.dto.RawMaterialsDTO;
import com.islandempires.resourcesservice.enums.TransportResourceStatusEnum;
import com.islandempires.resourcesservice.enums.TransportType;
import com.islandempires.resourcesservice.exception.CustomRunTimeException;
import com.islandempires.resourcesservice.exception.ExceptionE;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document("MutualTrading")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceTransport implements Serializable {

    @Id
    @NotBlank
    private String id;

    @Indexed
    private String senderIslandId;

    @Indexed
    private String receiverIslandId;

    private RawMaterialsDTO rawMaterials;

    private LocalDateTime localDateTime;

    private List<ResourceTransportState> resourceTransportStateList = new ArrayList<>();

    private int shipNumber;

    private TransportType transportType;

    private boolean isCanceled = false;

    public ResourceTransport(String senderIslandId, String receiverIslandId, RawMaterialsDTO rawMaterials,
                             LocalDateTime localDateTime, ResourceTransportState resourceTransportState, TransportType transportType) {
        this.senderIslandId = senderIslandId;
        this.receiverIslandId = receiverIslandId;
        this.rawMaterials = rawMaterials;
        this.localDateTime = localDateTime;
        this.transportType = transportType;
        this.addState(resourceTransportState);
    }

    public void addState(ResourceTransportState resourceTransportState) {
        resourceTransportStateList.add(resourceTransportState);
    }

    public Duration getDuration() {
        return resourceTransportStateList.get(0).getDuration();
    }

    public TransportResourceStatusEnum getState() {
        return resourceTransportStateList.size() > 0 ? resourceTransportStateList.get(resourceTransportStateList.size() - 1).getTransportResourceStatusEnum()
                : null;
    }
}
