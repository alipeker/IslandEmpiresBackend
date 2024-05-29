package com.islandempires.militaryservice.model.soldierStats;

import com.islandempires.militaryservice.enums.SoldierSubTypeEnum;
import com.islandempires.militaryservice.enums.SoldierTypeEnum;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Map;

@Entity(name = "MilitaryUnits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MilitaryUnits implements Serializable {
    @Id
    private SoldierSubTypeEnum id;

    private int attackPoint;

    private Map<SoldierTypeEnum, Integer> defensePoints;

    private RawMaterialsAndPopulationCost rawMaterialsAndPopulationCost;

}
