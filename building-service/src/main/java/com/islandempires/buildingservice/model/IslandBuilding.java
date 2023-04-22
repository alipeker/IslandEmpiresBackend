package com.islandempires.buildingservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.io.Serializable;
import java.math.BigInteger;

@Data
@Document("IslandBuilding")
@AllArgsConstructor
@NoArgsConstructor
public class IslandBuilding implements Serializable {
    @Id
    private BigInteger islandid;
    private Integer buildingId;
    private Long iron;
    private Long clay;
    private Long gold;
    private Long timestamp;
}

