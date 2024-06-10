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

    public BigInteger calculateTotalAttackPoint() {
        return soldierCount.multiply(BigInteger.valueOf(soldierBaseInfo.getAttackPoint()));
    }

    public BigDecimal calculateTotalDefencePointWithEnemySoldierRatios(SoldierRatios soldierRatios) {
        Double averageInfantrymanDefencePoint = soldierRatios.getInfantrymanToTotalSoldiersRatio() * soldierBaseInfo.getDefensePoints().get(SoldierTypeEnum.INFANTRYMAN);
        Double averageRifleDefencePoint = soldierRatios.getRiflesToTotalSoldiersRatio() * soldierBaseInfo.getDefensePoints().get(SoldierTypeEnum.RIFLE);
        Double averageCannonDefencePoint = soldierRatios.getCannonToTotalSoldiersRatio() * soldierBaseInfo.getDefensePoints().get(SoldierTypeEnum.CANNON);
        Double averageShipDefencePoint = soldierRatios.getShipsToTotalSoldiersRatio() * soldierBaseInfo.getDefensePoints().get(SoldierTypeEnum.SHIP);

        Double averageDefensePoint = averageInfantrymanDefencePoint + averageRifleDefencePoint + averageCannonDefencePoint + averageShipDefencePoint;

        return BigDecimal.valueOf(averageDefensePoint).multiply(new BigDecimal(soldierCount));
    }

    public SoldierTotalDefenceAgainstSoldierType calculateTotalDefencePointsEachSoldierType() {
        return new SoldierTotalDefenceAgainstSoldierType(
                new BigDecimal(soldierCount).multiply(BigDecimal.valueOf(soldierBaseInfo.getDefensePoints().get(SoldierTypeEnum.INFANTRYMAN))),
                new BigDecimal(soldierCount).multiply(BigDecimal.valueOf(soldierBaseInfo.getDefensePoints().get(SoldierTypeEnum.RIFLE))),
                new BigDecimal(soldierCount).multiply(BigDecimal.valueOf(soldierBaseInfo.getDefensePoints().get(SoldierTypeEnum.CANNON))),
                new BigDecimal(soldierCount).multiply(BigDecimal.valueOf(soldierBaseInfo.getDefensePoints().get(SoldierTypeEnum.SHIP)))
                );
    }

    public SoldierTotalDefenceAgainstSoldierType calculateTotalDefencePoints() {
        return new SoldierTotalDefenceAgainstSoldierType(
                new BigDecimal(soldierCount).multiply(BigDecimal.valueOf(soldierBaseInfo.getDefensePoints().get(SoldierTypeEnum.INFANTRYMAN))),
                new BigDecimal(soldierCount).multiply(BigDecimal.valueOf(soldierBaseInfo.getDefensePoints().get(SoldierTypeEnum.RIFLE))),
                new BigDecimal(soldierCount).multiply(BigDecimal.valueOf(soldierBaseInfo.getDefensePoints().get(SoldierTypeEnum.CANNON))),
                new BigDecimal(soldierCount).multiply(BigDecimal.valueOf(soldierBaseInfo.getDefensePoints().get(SoldierTypeEnum.SHIP))));
    }

    public Soldier addSoldier(BigInteger count) {
        this.soldierCount.add(count);
        return this;
    }
}
