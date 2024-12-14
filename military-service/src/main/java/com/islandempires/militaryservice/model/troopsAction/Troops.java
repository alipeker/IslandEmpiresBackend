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


    public SoldierTotalDefenceAgainstSoldierType calculateTotalDefencePointOfAllUnitsPerEachSoldierType(double defenseMultiplier) {
        SoldierTotalDefenceAgainstSoldierType totalDefencePoint = new SoldierTotalDefenceAgainstSoldierType();
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getPikeman().calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getAxeman().calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getArchers().calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getSwordsman().calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getLightArmedMusketeer().calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getMediumArmedMusketeer().calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getHeavyArmedMusketeer().calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getCulverin().calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getMortar().calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getRibault().calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getHolk().calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getGunHolk().calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        totalDefencePoint = totalDefencePoint.addPoints(militaryUnits.getCarrack().calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        return totalDefencePoint;
    }

    public SoldierRatios calculateRatioPerEachMainSoldierType() {
        return militaryUnits.calculateRatioPerEachSoldierType();
    }

    public BigInteger calculateTotalAttackPointOfAllUnits() {
        return militaryUnits.calculateTotalAttackPointOfAllUnits(militaryUnits.getOwner().getDefenceAndAttackMultiplier());
    }

    public TotalSoldierCount calculateTotalSoldierCount() {
        return militaryUnits.calculateTotalSoldierCount();
    }

    public SoldierTotalDefenceAgainstSoldierType calculateTotalDefencePointPerEachSoldierType(double defenseMultiplier){
        return militaryUnits.calculateTotalDefencePointPerEachSoldierType(defenseMultiplier);
    }

    public MilitaryUnitsKilledMilitaryUnitCountDTO killSoldiersWithTotalStrengthDifferencePointDefenceWin(TotalAttackPointForKillSoldierMainType totalAttackPointForKillSoldierMainType,
                                                                                                          SoldierTotalDefenceAgainstSoldierType soldierTotalDefenceAgainstSoldierType,
                                                                                                          GameServerSoldier gameServerSoldier) {
        return militaryUnits.killSoldiersWithTotalStrengthDifferencePointDefenceWin(totalAttackPointForKillSoldierMainType, soldierTotalDefenceAgainstSoldierType, gameServerSoldier);
    }


}

