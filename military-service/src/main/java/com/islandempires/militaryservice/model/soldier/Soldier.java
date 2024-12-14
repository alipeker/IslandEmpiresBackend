package com.islandempires.militaryservice.model.soldier;

import com.islandempires.militaryservice.dto.SoldierTotalDefenceAgainstSoldierType;
import com.islandempires.militaryservice.dto.SoldierRatios;
import com.islandempires.militaryservice.enums.SoldierTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;


@MappedSuperclass
@Data
@AllArgsConstructor
@EqualsAndHashCode
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public abstract class Soldier {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Soldier_generator")
    @SequenceGenerator(name="Soldier_generator", sequenceName = "Soldier_sequence", allocationSize=1)
    protected Long id;

    protected BigInteger soldierCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "soldierBaseInfo_id")
    private SoldierBaseInfo soldierBaseInfo;

    public Soldier() {
        this.soldierCount = BigInteger.ZERO;
    }

    public BigInteger calculateTotalAttackPoint(double defenseMultiplier) {
        return BigDecimal.valueOf(defenseMultiplier)
                .multiply(new BigDecimal(soldierCount.multiply(BigInteger.valueOf(soldierBaseInfo.getAttackPoint())))).toBigInteger();
    }

    public BigDecimal calculateTotalDefencePointWithEnemySoldierRatios(SoldierRatios soldierRatios, double defenseMultiplier) {
        Double averageInfantrymanDefencePoint = soldierRatios.getInfantrymanToTotalSoldiersRatio() * soldierBaseInfo.getDefensePoints().get(SoldierTypeEnum.INFANTRYMAN);
        Double averageRifleDefencePoint = soldierRatios.getRiflesToTotalSoldiersRatio() * soldierBaseInfo.getDefensePoints().get(SoldierTypeEnum.RIFLE);
        Double averageCannonDefencePoint = soldierRatios.getCannonToTotalSoldiersRatio() * soldierBaseInfo.getDefensePoints().get(SoldierTypeEnum.CANNON);
        Double averageShipDefencePoint = soldierRatios.getShipsToTotalSoldiersRatio() * soldierBaseInfo.getDefensePoints().get(SoldierTypeEnum.SHIP);

        double averageDefensePoint = averageInfantrymanDefencePoint + averageRifleDefencePoint + averageCannonDefencePoint + averageShipDefencePoint;

        return BigDecimal.valueOf(averageDefensePoint).multiply(new BigDecimal(soldierCount)).multiply(BigDecimal.valueOf(defenseMultiplier));
    }

    public SoldierTotalDefenceAgainstSoldierType calculateTotalDefencePointsEachSoldierType(double defenseMultiplier) {
        return new SoldierTotalDefenceAgainstSoldierType(
                new BigDecimal(soldierCount).multiply(BigDecimal.valueOf(soldierBaseInfo.getDefensePoints().get(SoldierTypeEnum.INFANTRYMAN)))
                        .multiply(BigDecimal.valueOf(defenseMultiplier)),
                new BigDecimal(soldierCount).multiply(BigDecimal.valueOf(soldierBaseInfo.getDefensePoints().get(SoldierTypeEnum.RIFLE)))
                        .multiply(BigDecimal.valueOf(defenseMultiplier)),
                new BigDecimal(soldierCount).multiply(BigDecimal.valueOf(soldierBaseInfo.getDefensePoints().get(SoldierTypeEnum.CANNON)))
                        .multiply(BigDecimal.valueOf(defenseMultiplier)),
                new BigDecimal(soldierCount).multiply(BigDecimal.valueOf(soldierBaseInfo.getDefensePoints().get(SoldierTypeEnum.SHIP)))
                        .multiply(BigDecimal.valueOf(defenseMultiplier))
                );
    }


    public Soldier addSoldier(BigInteger count) {
        this.soldierCount.add(count);
        return this;
    }
}
