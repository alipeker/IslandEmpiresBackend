package com.islandempires.militaryservice.model.resource;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RawMaterialsAndPopulationCost {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RawMaterialsAndPopulationCost_generator")
    @SequenceGenerator(name="RawMaterialsAndPopulationCost_generator", sequenceName = "RawMaterialsAndPopulationCost_sequence", allocationSize=1)
    protected Long id;

    private int wood;

    private int clay;

    private int iron;

    private int population;

}
