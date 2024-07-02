package com.islandempires.militaryservice.model.resource;

import com.islandempires.militaryservice.model.production.SoldierProduction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RawMaterialsAndPopulationCost implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RawMaterialsAndPopulationCost_generator")
    @SequenceGenerator(name="RawMaterialsAndPopulationCost_generator", sequenceName = "RawMaterialsAndPopulationCost_sequence", allocationSize=1)
    protected Long id;

    private int wood;

    private int clay;

    private int iron;

    private int population;

    @OneToOne(mappedBy = "rawMaterialsAndPopulationCost")
    private SoldierProduction soldierProduction;

    public RawMaterialsAndPopulationCost multiply(int multiplyNumber) {
        return new RawMaterialsAndPopulationCost(null, wood * multiplyNumber, clay * multiplyNumber, iron * multiplyNumber, population * multiplyNumber, null);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
