package com.islandempires.militaryservice.model.troopsAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.islandempires.militaryservice.dto.SoldierRatios;
import com.islandempires.militaryservice.dto.SoldierTotalDefenceAgainstSoldierType;
import com.islandempires.militaryservice.dto.TotalSoldierCount;
import com.islandempires.militaryservice.enums.SoldierTypeEnum;
import com.islandempires.militaryservice.model.MilitaryUnits;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Troops implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Troops_generator")
    @SequenceGenerator(name="Troops_generator", sequenceName = "Troops_sequence", allocationSize=1)
    protected Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "militaryUnitId", referencedColumnName = "id")
    protected MilitaryUnits militaryUnits;

    protected LocalDateTime startTime;

    public BigDecimal calculateTotalDefencePointOfAllUnits(SoldierRatios soldierRatios) {
        BigDecimal totalDefencePoint = BigDecimal.ZERO;
        totalDefencePoint = totalDefencePoint.add(militaryUnits.getPikeman().calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(militaryUnits.getAxeman().calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(militaryUnits.getArchers().calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(militaryUnits.getSwordsman().calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(militaryUnits.getLightArmedMusketeer().calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(militaryUnits.getMediumArmedMusketeer().calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(militaryUnits.getHeavyArmedMusketeer().calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(militaryUnits.getCulverin().calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(militaryUnits.getMortar().calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(militaryUnits.getRibault().calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(militaryUnits.getHolk().calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(militaryUnits.getGunHolk().calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(militaryUnits.getCarrack().calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        return totalDefencePoint;
    }

    public SoldierRatios calculateRatioPerEachMainSoldierType() {
        return militaryUnits.calculateRatioPerEachSoldierType();
    }

    public BigInteger calculateTotalAttackPointOfAllUnits() {
        return militaryUnits.calculateTotalAttackPointOfAllUnits();
    }

    public TotalSoldierCount calculateTotalSoldierCount() {
        return militaryUnits.calculateTotalSoldierCount();
    }

    public SoldierTotalDefenceAgainstSoldierType calculateTotalDefencePointPerEachSoldierType(){
        return militaryUnits.calculateTotalDefencePointPerEachSoldierType();
    }
}