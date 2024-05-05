package com.islandempires.buildingservice.model;

import com.islandempires.buildingservice.model.buildingtype.BaseStructures;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document("IslandBuilding")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IslandBuilding implements Serializable {
    @Id
    @NotBlank
    private String id;

    private String islandId;

    private List<BaseStructures> allBuildingList;
}
