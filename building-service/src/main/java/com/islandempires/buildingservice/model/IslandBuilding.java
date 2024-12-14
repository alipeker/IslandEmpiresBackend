package com.islandempires.buildingservice.model;

import com.islandempires.buildingservice.model.building.AllBuildings;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Duration;

@Document("IslandBuilding")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IslandBuilding implements Serializable {
    @Id
    private String id;

    private Long userId;

    private String serverId;

    private AllBuildings allBuilding;

    private int timeReductionPercentage = 100;

    private Duration refundTime;

    public Duration minusRefundTime(Duration minusTime) {
        if(minusTime.compareTo(refundTime) > 0) {
            Duration remaining = minusTime.minus(refundTime);
            refundTime = Duration.ZERO;
            return remaining;
        }
        refundTime = refundTime.minus(minusTime);
        return Duration.ofSeconds(1);
    }

    public Duration getRefundTime() {
        if(refundTime == null) {
            refundTime = Duration.ZERO;
        }
        return refundTime;
    }
}
