package com.islandempires.militaryservice.model.production;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.islandempires.militaryservice.enums.SoldierSubTypeEnum;
import com.islandempires.militaryservice.model.IslandMilitary;
import com.islandempires.militaryservice.model.resource.RawMaterialsAndPopulationCost;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Entity(name = "SoldierProduction")
@AllArgsConstructor
@NoArgsConstructor
public class SoldierProduction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SoldierProduction_generator")
    @SequenceGenerator(name="SoldierProduction_generator", sequenceName = "SoldierProduction_sequence", allocationSize=1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "islandMilitaryId", insertable = false, updatable = false)
    private IslandMilitary islandMilitary;

    private String islandMilitaryId;

    private SoldierSubTypeEnum soldierSubType;

    private int soldierCount;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "rawMaterialAndPopulationCostId", referencedColumnName = "id")
    @JsonBackReference
    private RawMaterialsAndPopulationCost rawMaterialsAndPopulationCost;


    private LocalDateTime time;

    private Duration productionDuration;

    private Boolean isActive = true;
}
