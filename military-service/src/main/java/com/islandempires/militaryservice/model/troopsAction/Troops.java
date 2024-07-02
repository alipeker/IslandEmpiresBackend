package com.islandempires.militaryservice.model.troopsAction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.islandempires.militaryservice.dto.*;
import com.islandempires.militaryservice.model.GameServerSoldier;
import com.islandempires.militaryservice.model.IslandMilitary;
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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
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

    public SoldierTotalDefenceAgainstSoldierType calculateTotalDefencePointOfAllUnitsPerEachSoldierType() {
        SoldierTotalDefenceAgainstSoldierType totalDefencePoint = new SoldierTotalDefenceAgainstSoldierType();
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getPikeman().calculateTotalDefencePointsEachSoldierType());
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getAxeman().calculateTotalDefencePointsEachSoldierType());
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getArchers().calculateTotalDefencePointsEachSoldierType());
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getSwordsman().calculateTotalDefencePointsEachSoldierType());
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getLightArmedMusketeer().calculateTotalDefencePointsEachSoldierType());
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getMediumArmedMusketeer().calculateTotalDefencePointsEachSoldierType());
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getHeavyArmedMusketeer().calculateTotalDefencePointsEachSoldierType());
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getCulverin().calculateTotalDefencePointsEachSoldierType());
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getMortar().calculateTotalDefencePointsEachSoldierType());
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getRibault().calculateTotalDefencePointsEachSoldierType());
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getHolk().calculateTotalDefencePointsEachSoldierType());
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getGunHolk().calculateTotalDefencePointsEachSoldierType());
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getCarrack().calculateTotalDefencePointsEachSoldierType());
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

    public MilitaryUnitsKilledMilitaryUnitCountDTO killSoldiersWithTotalStrengthDifferencePointDefenceWin(TotalAttackPointForKillSoldierMainType totalAttackPointForKillSoldierMainType,
                                                                                                          SoldierTotalDefenceAgainstSoldierType soldierTotalDefenceAgainstSoldierType,
                                                                                                          GameServerSoldier gameServerSoldier) {
        return militaryUnits.killSoldiersWithTotalStrengthDifferencePointDefenceWin(totalAttackPointForKillSoldierMainType, soldierTotalDefenceAgainstSoldierType, gameServerSoldier);
    }


}

