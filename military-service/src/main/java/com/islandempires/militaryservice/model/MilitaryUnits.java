package com.islandempires.militaryservice.model;

import com.islandempires.militaryservice.dto.*;
import com.islandempires.militaryservice.enums.SoldierSubTypeEnum;
import com.islandempires.militaryservice.enums.SoldierTypeEnum;
import com.islandempires.militaryservice.model.soldier.Soldier;
import com.islandempires.militaryservice.model.soldier.SoldierBaseInfo;
import com.islandempires.militaryservice.model.soldier.cannon.Cannon;
import com.islandempires.militaryservice.model.soldier.cannon.Culverin;
import com.islandempires.militaryservice.model.soldier.cannon.Mortar;
import com.islandempires.militaryservice.model.soldier.cannon.Ribault;
import com.islandempires.militaryservice.model.soldier.infantryman.*;
import com.islandempires.militaryservice.model.soldier.rifle.HeavyArmedMusketeer;
import com.islandempires.militaryservice.model.soldier.rifle.LightArmedMusketeer;
import com.islandempires.militaryservice.model.soldier.rifle.MediumArmedMusketeer;
import com.islandempires.militaryservice.model.soldier.rifle.Rifle;
import com.islandempires.militaryservice.model.soldier.ship.Carrack;
import com.islandempires.militaryservice.model.soldier.ship.GunHolk;
import com.islandempires.militaryservice.model.soldier.ship.Holk;
import com.islandempires.militaryservice.model.soldier.ship.Ship;
import com.islandempires.militaryservice.repository.GameServerSoldierBaseInfoRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MilitaryUnits {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MilitaryUnits_generator")
    @SequenceGenerator(name="MilitaryUnits_generator", sequenceName = "MilitaryUnits_sequence", allocationSize=1)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Pikeman pikeman;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Axeman axeman;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Archer archers;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Swordsman swordsman;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LightArmedMusketeer lightArmedMusketeer;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private MediumArmedMusketeer mediumArmedMusketeer;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private HeavyArmedMusketeer heavyArmedMusketeer;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Culverin culverin;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Mortar mortar;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Ribault ribault;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Holk holk;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private GunHolk gunHolk;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Carrack carrack;

    public void initialize(GameServerSoldier gameServerSoldier) {
        this.pikeman = createPikeman(
                gameServerSoldier.getSoldierBaseInfoList().stream().filter(soldierBaseInfo -> soldierBaseInfo.getSoldierSubTypeName().equals(SoldierSubTypeEnum.PIKEMAN.toString())).findFirst().orElseThrow());
        this.axeman = createAxeman(
                gameServerSoldier.getSoldierBaseInfoList().stream().filter(soldierBaseInfo -> soldierBaseInfo.getSoldierSubTypeName().equals(SoldierSubTypeEnum.AXEMAN.toString())).findFirst().orElseThrow());
        this.archers = createArcher(
                gameServerSoldier.getSoldierBaseInfoList().stream().filter(soldierBaseInfo -> soldierBaseInfo.getSoldierSubTypeName().equals(SoldierSubTypeEnum.ARCHER.toString())).findFirst().orElseThrow());
        this.swordsman = createSwordsman(
                gameServerSoldier.getSoldierBaseInfoList().stream().filter(soldierBaseInfo -> soldierBaseInfo.getSoldierSubTypeName().equals(SoldierSubTypeEnum.SWORDSMAN.toString())).findFirst().orElseThrow());
        this.lightArmedMusketeer = createLightArmedMusketeer(
                gameServerSoldier.getSoldierBaseInfoList().stream().filter(soldierBaseInfo -> soldierBaseInfo.getSoldierSubTypeName().equals(SoldierSubTypeEnum.LIGHT_ARMED_MUSKETEER.toString())).findFirst().orElseThrow());
        this.mediumArmedMusketeer = createMediumArmedMusketeer(
                gameServerSoldier.getSoldierBaseInfoList().stream().filter(soldierBaseInfo -> soldierBaseInfo.getSoldierSubTypeName().equals(SoldierSubTypeEnum.MEDIUM_ARMED_MUSKETEER.toString())).findFirst().orElseThrow());
        this.heavyArmedMusketeer = createHeavyArmedMusketeer(
                gameServerSoldier.getSoldierBaseInfoList().stream().filter(soldierBaseInfo -> soldierBaseInfo.getSoldierSubTypeName().equals(SoldierSubTypeEnum.HEAVY_ARMED_MUSKETEER.toString())).findFirst().orElseThrow());
        this.culverin = createCulverin(
                gameServerSoldier.getSoldierBaseInfoList().stream().filter(soldierBaseInfo -> soldierBaseInfo.getSoldierSubTypeName().equals(SoldierSubTypeEnum.CULVERIN.toString())).findFirst().orElseThrow());
        this.mortar = createMortar(
                gameServerSoldier.getSoldierBaseInfoList().stream().filter(soldierBaseInfo -> soldierBaseInfo.getSoldierSubTypeName().equals(SoldierSubTypeEnum.MORTAR.toString())).findFirst().orElseThrow());
        this.ribault = createRibault(
                gameServerSoldier.getSoldierBaseInfoList().stream().filter(soldierBaseInfo -> soldierBaseInfo.getSoldierSubTypeName().equals(SoldierSubTypeEnum.RIBAULT.toString())).findFirst().orElseThrow());
        this.holk = createHolk(
                gameServerSoldier.getSoldierBaseInfoList().stream().filter(soldierBaseInfo -> soldierBaseInfo.getSoldierSubTypeName().equals(SoldierSubTypeEnum.HOLK.toString())).findFirst().orElseThrow());
        this.gunHolk = createGunHolk(
                gameServerSoldier.getSoldierBaseInfoList().stream().filter(soldierBaseInfo -> soldierBaseInfo.getSoldierSubTypeName().equals(SoldierSubTypeEnum.GUN_HOLK.toString())).findFirst().orElseThrow());
        this.carrack = createCarrack(
                gameServerSoldier.getSoldierBaseInfoList().stream().filter(soldierBaseInfo -> soldierBaseInfo.getSoldierSubTypeName().equals(SoldierSubTypeEnum.CARRACK.toString())).findFirst().orElseThrow());
    }

    private Pikeman createPikeman(SoldierBaseInfo soldierBaseInfo) {
        Pikeman newPikeman = new Pikeman();
        newPikeman.setSoldierCount(BigInteger.ZERO);
        newPikeman.setSoldierBaseInfo(soldierBaseInfo);
        return newPikeman;
    }

    private Axeman createAxeman(SoldierBaseInfo soldierBaseInfo) {
        Axeman newAxeman = new Axeman();
        newAxeman.setSoldierCount(BigInteger.ZERO);
        newAxeman.setSoldierBaseInfo(soldierBaseInfo);
        return newAxeman;
    }

    private Archer createArcher(SoldierBaseInfo soldierBaseInfo) {
        Archer newArcher = new Archer();
        newArcher.setSoldierCount(BigInteger.ZERO);
        newArcher.setSoldierBaseInfo(soldierBaseInfo);
        return newArcher;
    }

    private Swordsman createSwordsman(SoldierBaseInfo soldierBaseInfo) {
        Swordsman newSwordsman = new Swordsman();
        newSwordsman.setSoldierCount(BigInteger.ZERO);
        newSwordsman.setSoldierBaseInfo(soldierBaseInfo);
        return newSwordsman;
    }

    private LightArmedMusketeer createLightArmedMusketeer(SoldierBaseInfo soldierBaseInfo) {
        LightArmedMusketeer newLightArmedMusketeer = new LightArmedMusketeer();
        newLightArmedMusketeer.setSoldierCount(BigInteger.ZERO);
        newLightArmedMusketeer.setSoldierBaseInfo(soldierBaseInfo);
        return newLightArmedMusketeer;
    }

    private MediumArmedMusketeer createMediumArmedMusketeer(SoldierBaseInfo soldierBaseInfo) {
        MediumArmedMusketeer newMediumArmedMusketeer = new MediumArmedMusketeer();
        newMediumArmedMusketeer.setSoldierCount(BigInteger.ZERO);
        newMediumArmedMusketeer.setSoldierBaseInfo(soldierBaseInfo);
        return newMediumArmedMusketeer;
    }

    private HeavyArmedMusketeer createHeavyArmedMusketeer(SoldierBaseInfo soldierBaseInfo) {
        HeavyArmedMusketeer newHeavyArmedMusketeer = new HeavyArmedMusketeer();
        newHeavyArmedMusketeer.setSoldierCount(BigInteger.ZERO);
        newHeavyArmedMusketeer.setSoldierBaseInfo(soldierBaseInfo);
        return newHeavyArmedMusketeer;
    }

    private Culverin createCulverin(SoldierBaseInfo soldierBaseInfo) {
        Culverin newCulverin = new Culverin();
        newCulverin.setSoldierCount(BigInteger.ZERO);
        newCulverin.setSoldierBaseInfo(soldierBaseInfo);
        return newCulverin;
    }

    private Mortar createMortar(SoldierBaseInfo soldierBaseInfo) {
        Mortar newMortar = new Mortar();
        newMortar.setSoldierCount(BigInteger.ZERO);
        newMortar.setSoldierBaseInfo(soldierBaseInfo);
        return newMortar;
    }

    private Ribault createRibault(SoldierBaseInfo soldierBaseInfo) {
        Ribault newRibault = new Ribault();
        newRibault.setSoldierCount(BigInteger.ZERO);
        newRibault.setSoldierBaseInfo(soldierBaseInfo);
        return newRibault;
    }

    private Holk createHolk(SoldierBaseInfo soldierBaseInfo) {
        Holk newHolk = new Holk();
        newHolk.setSoldierCount(BigInteger.ZERO);
        newHolk.setSoldierBaseInfo(soldierBaseInfo);
        return newHolk;
    }

    private GunHolk createGunHolk(SoldierBaseInfo soldierBaseInfo) {
        GunHolk newGunHolk = new GunHolk();
        newGunHolk.setSoldierCount(BigInteger.ZERO);
        newGunHolk.setSoldierBaseInfo(soldierBaseInfo);
        return newGunHolk;
    }

    private Carrack createCarrack(SoldierBaseInfo soldierBaseInfo) {
        Carrack newCarrack = new Carrack();
        newCarrack.setSoldierCount(BigInteger.ZERO);
        newCarrack.setSoldierBaseInfo(soldierBaseInfo);
        return newCarrack;
    }

    public BigInteger getInfantrymanNumber() {
        return swordsman.getSoldierCount()
                .add(pikeman.getSoldierCount())
                .add(archers.getSoldierCount())
                .add(axeman.getSoldierCount());
    }

    public BigInteger getRifleNumber() {
        return lightArmedMusketeer.getSoldierCount()
                .add(mediumArmedMusketeer.getSoldierCount())
                .add(heavyArmedMusketeer.getSoldierCount());
    }

    public BigInteger getCannonNumber() {
        return culverin.getSoldierCount()
                .add(mortar.getSoldierCount())
                .add(ribault.getSoldierCount());
    }

    public BigInteger getShipNumber() {
        return holk.getSoldierCount()
                .add(gunHolk.getSoldierCount())
                .add(carrack.getSoldierCount());
    }

    public BigInteger getTotalSoldiers() {
        return getInfantrymanNumber()
                .add(getRifleNumber())
                .add(getCannonNumber())
                .add(getShipNumber());
    }

    /*
    public BigInteger getInfantrymenTotalAttackPoint() {
        return swordsman.calculateTotalAttackPoint()
                .add(archers.calculateTotalAttackPoint())
                .add(axeman.calculateTotalAttackPoint())
                .add(pikeman.calculateTotalAttackPoint());
    }

    public BigInteger getInfantrymenTotalAttackPoint() {
        return swordsman.calculateTotalAttackPoint()
                .add(archers.calculateTotalAttackPoint())
                .add(axeman.calculateTotalAttackPoint())
                .add(pikeman.calculateTotalAttackPoint());
    }

    public BigInteger getInfantrymenTotalAttackPoint() {
        return swordsman.calculateTotalAttackPoint()
                .add(archers.calculateTotalAttackPoint())
                .add(axeman.calculateTotalAttackPoint())
                .add(pikeman.calculateTotalAttackPoint());
    }

    public BigInteger getInfantrymenTotalAttackPoint() {
        return swordsman.calculateTotalAttackPoint()
                .add(archers.calculateTotalAttackPoint())
                .add(axeman.calculateTotalAttackPoint())
                .add(pikeman.calculateTotalAttackPoint());
    }*/

    public SoldierTotalDefenceAgainstSoldierType calculateTotalDefencePointPerEachSoldierType() {
        return pikeman.calculateTotalDefencePoints().addPoints(axeman.calculateTotalDefencePoints()).addPoints(archers.calculateTotalDefencePoints())
        .addPoints(swordsman.calculateTotalDefencePoints()).addPoints(lightArmedMusketeer.calculateTotalDefencePoints()).addPoints(mediumArmedMusketeer.calculateTotalDefencePoints())
        .addPoints(heavyArmedMusketeer.calculateTotalDefencePoints()).addPoints(culverin.calculateTotalDefencePoints()).addPoints(mortar.calculateTotalDefencePoints())
        .addPoints(ribault.calculateTotalDefencePoints()).addPoints(holk.calculateTotalDefencePoints()).addPoints(gunHolk.calculateTotalDefencePoints())
        .addPoints(carrack.calculateTotalDefencePoints());
    }

    public SoldierRatios calculateRatioPerEachSoldierType() {
        BigInteger totalSoldiers = getTotalSoldiers();

        BigInteger infantrymanNumber = getInfantrymanNumber();
        BigInteger rifleNumber = getRifleNumber();
        BigInteger cannonNumber = getCannonNumber();
        BigInteger shipNumber = getShipNumber();

        BigDecimal totalSoldiersDecimal = new BigDecimal(totalSoldiers);

        BigDecimal infantrymanRatioBigDecimal = infantrymanNumber.compareTo(BigInteger.ZERO) > 0
                ? new BigDecimal(infantrymanNumber).divide(totalSoldiersDecimal, 10, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        BigDecimal rifleRatioBigDecimal = rifleNumber.compareTo(BigInteger.ZERO) > 0
                ? new BigDecimal(rifleNumber).divide(totalSoldiersDecimal, 10, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        BigDecimal cannonRatioBigDecimal = cannonNumber.compareTo(BigInteger.ZERO) > 0
                ? new BigDecimal(cannonNumber).divide(totalSoldiersDecimal, 10, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        BigDecimal shipRatioBigDecimal = shipNumber.compareTo(BigInteger.ZERO) > 0
                ? new BigDecimal(shipNumber).divide(totalSoldiersDecimal, 10, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        double infantrymanRatio = infantrymanRatioBigDecimal.doubleValue();
        double rifleRatio = rifleRatioBigDecimal.doubleValue();
        double cannonRatio = cannonRatioBigDecimal.doubleValue();
        double shipRatio = shipRatioBigDecimal.doubleValue();

        return new SoldierRatios(infantrymanRatio, rifleRatio, cannonRatio, shipRatio);
    }

    public BigDecimal calculateTotalDefencePointOfAllUnits(SoldierRatios soldierRatios) {
        BigDecimal totalDefencePoint = BigDecimal.ZERO;
        totalDefencePoint = totalDefencePoint.add(pikeman.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(axeman.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(archers.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(swordsman.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(lightArmedMusketeer.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(mediumArmedMusketeer.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(heavyArmedMusketeer.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(culverin.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(mortar.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(ribault.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(holk.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(gunHolk.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        totalDefencePoint = totalDefencePoint.add(carrack.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios));
        return totalDefencePoint;
    }

    public BigInteger calculateTotalAttackPointOfAllUnits() {
        BigInteger totalAttackPoint = BigInteger.ZERO;

        totalAttackPoint = totalAttackPoint.add(pikeman.calculateTotalAttackPoint());
        totalAttackPoint = totalAttackPoint.add(axeman.calculateTotalAttackPoint());
        totalAttackPoint = totalAttackPoint.add(archers.calculateTotalAttackPoint());
        totalAttackPoint = totalAttackPoint.add(swordsman.calculateTotalAttackPoint());
        totalAttackPoint = totalAttackPoint.add(lightArmedMusketeer.calculateTotalAttackPoint());
        totalAttackPoint = totalAttackPoint.add(mediumArmedMusketeer.calculateTotalAttackPoint());
        totalAttackPoint = totalAttackPoint.add(heavyArmedMusketeer.calculateTotalAttackPoint());
        totalAttackPoint = totalAttackPoint.add(culverin.calculateTotalAttackPoint());
        totalAttackPoint = totalAttackPoint.add(mortar.calculateTotalAttackPoint());
        totalAttackPoint = totalAttackPoint.add(ribault.calculateTotalAttackPoint());
        totalAttackPoint = totalAttackPoint.add(holk.calculateTotalAttackPoint());
        totalAttackPoint = totalAttackPoint.add(gunHolk.calculateTotalAttackPoint());
        totalAttackPoint = totalAttackPoint.add(carrack.calculateTotalAttackPoint());

        return totalAttackPoint;
    }

    public TotalSoldierCount calculateTotalSoldierCount() {
        return new TotalSoldierCount(getInfantrymanNumber(), getRifleNumber(), getCannonNumber(), getShipNumber());
    }

    public void diminishingMilitaryUnitsCount(MilitaryUnits diminishingMilitaryUnits) {
        pikeman.setSoldierCount(pikeman.getSoldierCount().subtract(diminishingMilitaryUnits.getPikeman().getSoldierCount()));
        axeman.setSoldierCount(axeman.getSoldierCount().subtract(diminishingMilitaryUnits.getAxeman().getSoldierCount()));
        archers.setSoldierCount(archers.getSoldierCount().subtract(diminishingMilitaryUnits.getArchers().getSoldierCount()));
        swordsman.setSoldierCount(swordsman.getSoldierCount().subtract(diminishingMilitaryUnits.getSwordsman().getSoldierCount()));
        lightArmedMusketeer.setSoldierCount(lightArmedMusketeer.getSoldierCount().subtract(diminishingMilitaryUnits.getLightArmedMusketeer().getSoldierCount()));
        mediumArmedMusketeer.setSoldierCount(mediumArmedMusketeer.getSoldierCount().subtract(diminishingMilitaryUnits.getMediumArmedMusketeer().getSoldierCount()));
        heavyArmedMusketeer.setSoldierCount(heavyArmedMusketeer.getSoldierCount().subtract(diminishingMilitaryUnits.getHeavyArmedMusketeer().getSoldierCount()));
        culverin.setSoldierCount(culverin.getSoldierCount().subtract(diminishingMilitaryUnits.getCulverin().getSoldierCount()));
        mortar.setSoldierCount(mortar.getSoldierCount().subtract(diminishingMilitaryUnits.getMortar().getSoldierCount()));
        ribault.setSoldierCount(ribault.getSoldierCount().subtract(diminishingMilitaryUnits.getRibault().getSoldierCount()));
        holk.setSoldierCount(holk.getSoldierCount().subtract(diminishingMilitaryUnits.getHolk().getSoldierCount()));
        gunHolk.setSoldierCount(gunHolk.getSoldierCount().subtract(diminishingMilitaryUnits.getGunHolk().getSoldierCount()));
        carrack.setSoldierCount(carrack.getSoldierCount().subtract(diminishingMilitaryUnits.getCarrack().getSoldierCount()));
    }

    public void addMilitaryUnitsCount(MilitaryUnits addMilitaryUnits) {
        pikeman.setSoldierCount(pikeman.getSoldierCount().add(addMilitaryUnits.getPikeman().getSoldierCount()));
        axeman.setSoldierCount(axeman.getSoldierCount().add(addMilitaryUnits.getAxeman().getSoldierCount()));
        archers.setSoldierCount(archers.getSoldierCount().add(addMilitaryUnits.getArchers().getSoldierCount()));
        swordsman.setSoldierCount(swordsman.getSoldierCount().add(addMilitaryUnits.getSwordsman().getSoldierCount()));
        lightArmedMusketeer.setSoldierCount(lightArmedMusketeer.getSoldierCount().add(addMilitaryUnits.getLightArmedMusketeer().getSoldierCount()));
        mediumArmedMusketeer.setSoldierCount(mediumArmedMusketeer.getSoldierCount().add(addMilitaryUnits.getMediumArmedMusketeer().getSoldierCount()));
        heavyArmedMusketeer.setSoldierCount(heavyArmedMusketeer.getSoldierCount().add(addMilitaryUnits.getHeavyArmedMusketeer().getSoldierCount()));
        culverin.setSoldierCount(culverin.getSoldierCount().add(addMilitaryUnits.getCulverin().getSoldierCount()));
        mortar.setSoldierCount(mortar.getSoldierCount().add(addMilitaryUnits.getMortar().getSoldierCount()));
        ribault.setSoldierCount(ribault.getSoldierCount().add(addMilitaryUnits.getRibault().getSoldierCount()));
        holk.setSoldierCount(holk.getSoldierCount().add(addMilitaryUnits.getHolk().getSoldierCount()));
        gunHolk.setSoldierCount(gunHolk.getSoldierCount().add(addMilitaryUnits.getGunHolk().getSoldierCount()));
        carrack.setSoldierCount(carrack.getSoldierCount().add(addMilitaryUnits.getCarrack().getSoldierCount()));
    }

    // soldierAttackPoint * x = totalAttackPointForKillInfantrymanSubType
    public KillSoldierSubType killSoldierBySubType(Soldier soldier, BigDecimal totalAttackPointForKillInfantrymanSubType) {
        BigInteger totalKilledSoldier = totalAttackPointForKillInfantrymanSubType.divide(BigDecimal.valueOf(soldier.getSoldierBaseInfo().getAttackPoint()),10, RoundingMode.HALF_UP).toBigInteger();
        totalKilledSoldier = soldier.getSoldierCount().compareTo(totalKilledSoldier) > 0 ? totalKilledSoldier : soldier.getSoldierCount();
        soldier.setSoldierCount(soldier.getSoldierCount().subtract(totalKilledSoldier));
        return new KillSoldierSubType(soldier, totalKilledSoldier);
    }

    /*
    public KillSoldierType killSoldierByType(List<Soldier> soldiers, BigInteger totalAttackPointForKillInfantryman, MilitaryUnits militaryUnits) {
        BigInteger totalAttackPointForKillInfantrymanSubType = totalAttackPointForKillInfantryman.divide(BigInteger.valueOf(4));
        soldiers.stream().forEach(soldier -> {
            KillSoldierSubType killSoldierSubType = killSoldierBySubType(soldier,totalAttackPointForKillInfantrymanSubType);
        });
        return new KillSoldierType(soldiers,totalAttackPointForKillInfantryman);
    }*/

    public MilitaryUnits addSoldier(SoldierSubTypeEnum soldierTypeEnum, BigInteger count) {
        if(soldierTypeEnum.equals(SoldierSubTypeEnum.PIKEMAN)) {
            BigInteger totalSoldierCount = pikeman.getSoldierCount().add(count).compareTo(BigInteger.ZERO) < 0
                    ? BigInteger.ZERO
                    : pikeman.getSoldierCount().add(count);
            pikeman.setSoldierCount(totalSoldierCount);
        } else if(soldierTypeEnum.equals(SoldierSubTypeEnum.ARCHER)) {
            BigInteger totalSoldierCount = archers.getSoldierCount().add(count).compareTo(BigInteger.ZERO) < 0
                    ? BigInteger.ZERO
                    : archers.getSoldierCount().add(count);
            archers.setSoldierCount(totalSoldierCount);
        } else if(soldierTypeEnum.equals(SoldierSubTypeEnum.AXEMAN)) {
            BigInteger totalSoldierCount = axeman.getSoldierCount().add(count).compareTo(BigInteger.ZERO) < 0
                    ? BigInteger.ZERO
                    : axeman.getSoldierCount().add(count);
            axeman.setSoldierCount(totalSoldierCount);
        } else if(soldierTypeEnum.equals(SoldierSubTypeEnum.SWORDSMAN)) {
            BigInteger totalSoldierCount = swordsman.getSoldierCount().add(count).compareTo(BigInteger.ZERO) < 0
                    ? BigInteger.ZERO
                    : swordsman.getSoldierCount().add(count);
            swordsman.setSoldierCount(totalSoldierCount);
        } else if(soldierTypeEnum.equals(SoldierSubTypeEnum.LIGHT_ARMED_MUSKETEER)) {
            BigInteger totalSoldierCount = lightArmedMusketeer.getSoldierCount().add(count).compareTo(BigInteger.ZERO) < 0
                    ? BigInteger.ZERO
                    : lightArmedMusketeer.getSoldierCount().add(count);
            lightArmedMusketeer.setSoldierCount(totalSoldierCount);
        } else if(soldierTypeEnum.equals(SoldierSubTypeEnum.MEDIUM_ARMED_MUSKETEER)) {
            BigInteger totalSoldierCount = mediumArmedMusketeer.getSoldierCount().add(count).compareTo(BigInteger.ZERO) < 0
                    ? BigInteger.ZERO
                    : mediumArmedMusketeer.getSoldierCount().add(count);
            mediumArmedMusketeer.setSoldierCount(totalSoldierCount);
        } else if(soldierTypeEnum.equals(SoldierSubTypeEnum.HEAVY_ARMED_MUSKETEER)) {
            BigInteger totalSoldierCount = heavyArmedMusketeer.getSoldierCount().add(count).compareTo(BigInteger.ZERO) < 0
                    ? BigInteger.ZERO
                    : heavyArmedMusketeer.getSoldierCount().add(count);
            heavyArmedMusketeer.setSoldierCount(totalSoldierCount);
        } else if(soldierTypeEnum.equals(SoldierSubTypeEnum.CULVERIN)) {
            BigInteger totalSoldierCount = culverin.getSoldierCount().add(count).compareTo(BigInteger.ZERO) < 0
                    ? BigInteger.ZERO
                    : culverin.getSoldierCount().add(count);
            culverin.setSoldierCount(totalSoldierCount);
        } else if(soldierTypeEnum.equals(SoldierSubTypeEnum.MORTAR)) {
            BigInteger totalSoldierCount = mortar.getSoldierCount().add(count).compareTo(BigInteger.ZERO) < 0
                    ? BigInteger.ZERO
                    : mortar.getSoldierCount().add(count);
            mortar.setSoldierCount(totalSoldierCount);
        } else if(soldierTypeEnum.equals(SoldierSubTypeEnum.RIBAULT)) {
            BigInteger totalSoldierCount = ribault.getSoldierCount().add(count).compareTo(BigInteger.ZERO) < 0
                    ? BigInteger.ZERO
                    : ribault.getSoldierCount().add(count);
            ribault.setSoldierCount(totalSoldierCount);
        } else if(soldierTypeEnum.equals(SoldierSubTypeEnum.HOLK)) {
            BigInteger totalSoldierCount = holk.getSoldierCount().add(count).compareTo(BigInteger.ZERO) < 0
                    ? BigInteger.ZERO
                    : holk.getSoldierCount().add(count);
            holk.setSoldierCount(totalSoldierCount);
        } else if(soldierTypeEnum.equals(SoldierSubTypeEnum.GUN_HOLK)) {
            BigInteger totalSoldierCount = gunHolk.getSoldierCount().add(count).compareTo(BigInteger.ZERO) < 0
                    ? BigInteger.ZERO
                    : gunHolk.getSoldierCount().add(count);
            gunHolk.setSoldierCount(totalSoldierCount);
        } else if(soldierTypeEnum.equals(SoldierSubTypeEnum.CARRACK)) {
            BigInteger totalSoldierCount = carrack.getSoldierCount().add(count).compareTo(BigInteger.ZERO) < 0
                    ? BigInteger.ZERO
                    : carrack.getSoldierCount().add(count);
            carrack.setSoldierCount(totalSoldierCount);
        }
        return this;
    }

    public MilitaryUnitsKilledMilitaryUnitCountDTO killSoldierWithAverageAttackPointOfEnemy(List<Soldier> soldiers, MilitaryUnitsKilledMilitaryUnitCountDTO militaryUnitsKilledMilitaryUnitCountDTO) {
        MilitaryUnits kiledMilitaryUnits = militaryUnitsKilledMilitaryUnitCountDTO.getMilitaryUnits();
        TotalAttackPointForKillSoldierMainType totalAttackPointForKillSoldierMainType = militaryUnitsKilledMilitaryUnitCountDTO.getTotalAttackPointForKillSoldierMainType();
        for(int i=0; i<soldiers.size(); i++){
            Soldier soldier = soldiers.get(i);
            if(soldier instanceof Infantryman) {
                KillSoldierSubType killSoldierSubType = killSoldierBySubType(soldier, totalAttackPointForKillSoldierMainType.getTotalAttackPointForKillInfantrymanSubTypes().get(SoldierSubTypeEnum.valueOf(soldier.getSoldierBaseInfo().getSoldierSubTypeName())));
                totalAttackPointForKillSoldierMainType = totalAttackPointForKillSoldierMainType
                        .subtractInfantryManSubtype(SoldierSubTypeEnum.valueOf(soldier.getSoldierBaseInfo().getSoldierSubTypeName()),
                                new BigDecimal(killSoldierSubType.getTotalKilledSoldier()).multiply(new BigDecimal(soldier.getSoldierBaseInfo().getAttackPoint())));
                kiledMilitaryUnits = kiledMilitaryUnits.addSoldier(SoldierSubTypeEnum.valueOf(soldier.getSoldierBaseInfo().getSoldierSubTypeName()), killSoldierSubType.getTotalKilledSoldier());
            } else if(soldier instanceof Rifle) {
                KillSoldierSubType killSoldierSubType = killSoldierBySubType(soldier, totalAttackPointForKillSoldierMainType.getTotalAttackPointForKillRifleSubTypes().get(SoldierSubTypeEnum.valueOf(soldier.getSoldierBaseInfo().getSoldierSubTypeName())));
                totalAttackPointForKillSoldierMainType = totalAttackPointForKillSoldierMainType
                        .subtractRifleSubtype(SoldierSubTypeEnum.valueOf(soldier.getSoldierBaseInfo().getSoldierSubTypeName()),
                                new BigDecimal(killSoldierSubType.getTotalKilledSoldier()).multiply(new BigDecimal(soldier.getSoldierBaseInfo().getAttackPoint())));
                kiledMilitaryUnits = kiledMilitaryUnits.addSoldier(SoldierSubTypeEnum.valueOf(soldier.getSoldierBaseInfo().getSoldierSubTypeName()), killSoldierSubType.getTotalKilledSoldier());
            } else if(soldier instanceof Cannon) {
                KillSoldierSubType killSoldierSubType = killSoldierBySubType(soldier, totalAttackPointForKillSoldierMainType.getTotalAttackPointForKillCannonSubTypes().get(SoldierSubTypeEnum.valueOf(soldier.getSoldierBaseInfo().getSoldierSubTypeName())));
                totalAttackPointForKillSoldierMainType = totalAttackPointForKillSoldierMainType
                        .subtractCannonSubtype(SoldierSubTypeEnum.valueOf(soldier.getSoldierBaseInfo().getSoldierSubTypeName()),
                                new BigDecimal(killSoldierSubType.getTotalKilledSoldier()).multiply(new BigDecimal(soldier.getSoldierBaseInfo().getAttackPoint())));
                kiledMilitaryUnits = kiledMilitaryUnits.addSoldier(SoldierSubTypeEnum.valueOf(soldier.getSoldierBaseInfo().getSoldierSubTypeName()), killSoldierSubType.getTotalKilledSoldier());
            } else if(soldier instanceof Ship) {
                KillSoldierSubType killSoldierSubType = killSoldierBySubType(soldier, totalAttackPointForKillSoldierMainType.getTotalAttackPointForKillShipSubTypes().get(SoldierSubTypeEnum.valueOf(soldier.getSoldierBaseInfo().getSoldierSubTypeName())));
                totalAttackPointForKillSoldierMainType = totalAttackPointForKillSoldierMainType
                        .subtractShipSubtype(SoldierSubTypeEnum.valueOf(soldier.getSoldierBaseInfo().getSoldierSubTypeName()),
                                new BigDecimal(killSoldierSubType.getTotalKilledSoldier()).multiply(new BigDecimal(soldier.getSoldierBaseInfo().getAttackPoint())));
                kiledMilitaryUnits = kiledMilitaryUnits.addSoldier(SoldierSubTypeEnum.valueOf(soldier.getSoldierBaseInfo().getSoldierSubTypeName()), killSoldierSubType.getTotalKilledSoldier());
            }
        }
        militaryUnitsKilledMilitaryUnitCountDTO.setMilitaryUnits(kiledMilitaryUnits);
        militaryUnitsKilledMilitaryUnitCountDTO.setTotalAttackPointForKillSoldierMainType(totalAttackPointForKillSoldierMainType);
        return militaryUnitsKilledMilitaryUnitCountDTO;
    }

    public MilitaryUnits killSoldierWithAttackPointOfEnemy(List<Soldier> soldiers, BigDecimal totalAttackPoint, MilitaryUnits kiledMilitaryUnits) {
        for(int i=0; i<soldiers.size(); i++) {
            if(totalAttackPoint.compareTo(BigDecimal.ZERO) <= 0 ||
                    soldiers.stream().map(Soldier::calculateTotalAttackPoint).reduce(BigInteger.ZERO, BigInteger::add).compareTo(BigInteger.ZERO) <= 0) {
                return kiledMilitaryUnits;
            }
            Soldier soldier = soldiers.get(i);
            KillSoldierSubType killSoldierSubType = killSoldierBySubType(soldier, totalAttackPoint);
            totalAttackPoint = totalAttackPoint.subtract(new BigDecimal(killSoldierSubType.getTotalKilledSoldier())
                    .multiply(new BigDecimal(killSoldierSubType.getSoldier().getSoldierBaseInfo().getAttackPoint())));
            kiledMilitaryUnits = kiledMilitaryUnits.addSoldier(SoldierSubTypeEnum.valueOf(soldier.getSoldierBaseInfo().getSoldierSubTypeName()), killSoldierSubType.getSoldier().getSoldierCount());
        }
        return kiledMilitaryUnits;
    }
/*
    public MilitaryUnits killRemainderSoldiers(List<Soldier> soldiers, MilitaryUnits kiledMilitaryUnits, BigInteger remainderTotalAttackPoint) {
        int i = 0;
        BigInteger totalSoldierCount = soldiers.stream()
                .map(Soldier::getSoldierCount)
                .reduce(BigInteger.ZERO, BigInteger::add);
        while(remainderTotalAttackPoint.compareTo(BigInteger.ONE) <= 0 || totalSoldierCount.compareTo(BigInteger.ZERO) == 0){
            if(soldiers.size() - 1 == i) {
                i = 0;
            }
            Soldier soldier = soldiers.get(i);
            soldier.setSoldierCount(soldier.getSoldierCount().subtract(BigInteger.ZERO));
            remainderTotalAttackPoint = remainderTotalAttackPoint.subtract(soldier.calculateTotalAttackPoint());
            totalSoldierCount = totalSoldierCount.subtract(BigInteger.ONE);
            kiledMilitaryUnits.set
            i++;
        }
        return kiledMilitaryUnits;
    }*/

    public void killSoldiersWithTotalStrengthDifferencePoint(BigDecimal enemyDefencePointDivideAttackDefenceRatio, double killRatio,
                                                             SoldierTotalDefenceAgainstSoldierType enemySoldierTotalDefenceAgainstSoldierType, GameServerSoldier gameServerSoldier) {
        BigDecimal totalDefencePoint = enemySoldierTotalDefenceAgainstSoldierType.getInfantrymanDefencePoint().add(enemySoldierTotalDefenceAgainstSoldierType.getRiflesDefencePoint())
                .add(enemySoldierTotalDefenceAgainstSoldierType.getCannonDefencePoint()).add(enemySoldierTotalDefenceAgainstSoldierType.getShipDefencePoint());

        totalDefencePoint = totalDefencePoint.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : totalDefencePoint;

        BigDecimal totalAttackPointForKillSoldier = enemyDefencePointDivideAttackDefenceRatio.divide(BigDecimal.valueOf(killRatio), 10, RoundingMode.HALF_UP);
        BigDecimal totalAttackPointForKillInfantryman = enemySoldierTotalDefenceAgainstSoldierType.getInfantrymanDefencePoint().divide(totalDefencePoint, 10, RoundingMode.HALF_UP).multiply(totalAttackPointForKillSoldier);
        BigDecimal totalAttackPointForKillRifle = enemySoldierTotalDefenceAgainstSoldierType.getRiflesDefencePoint().divide(totalDefencePoint, 10, RoundingMode.HALF_UP).multiply(totalAttackPointForKillSoldier);
        BigDecimal totalAttackPointForKillCannon = enemySoldierTotalDefenceAgainstSoldierType.getCannonDefencePoint().divide(totalDefencePoint, 10, RoundingMode.HALF_UP).multiply(totalAttackPointForKillSoldier);
        BigDecimal totalAttackPointForKillShip = enemySoldierTotalDefenceAgainstSoldierType.getShipDefencePoint().divide(totalDefencePoint, 10, RoundingMode.HALF_UP).multiply(totalAttackPointForKillSoldier);

        List<Soldier> soldiers = new ArrayList<>();
        soldiers.add(pikeman);
        soldiers.add(axeman);
        soldiers.add(archers);
        soldiers.add(swordsman);
        soldiers.add(lightArmedMusketeer);
        soldiers.add(mediumArmedMusketeer);
        soldiers.add(heavyArmedMusketeer);
        soldiers.add(culverin);
        soldiers.add(mortar);
        soldiers.add(ribault);
        soldiers.add(holk);
        soldiers.add(gunHolk);
        soldiers.add(carrack);

        TotalAttackPointForKillSoldierMainType totalAttackPointForKillSoldierMainType = new TotalAttackPointForKillSoldierMainType();
        totalAttackPointForKillSoldierMainType.setTotalAttackPointForKillInfantryman(totalAttackPointForKillInfantryman);
        totalAttackPointForKillSoldierMainType.setTotalAttackPointForKillRifle(totalAttackPointForKillRifle);
        totalAttackPointForKillSoldierMainType.setTotalAttackPointForKillCannon(totalAttackPointForKillCannon);
        totalAttackPointForKillSoldierMainType.setTotalAttackPointForKillShip(totalAttackPointForKillShip);

        MilitaryUnits kiledMilitaryUnits = new MilitaryUnits();
        kiledMilitaryUnits.initialize(gameServerSoldier);
        MilitaryUnitsKilledMilitaryUnitCountDTO militaryUnitsKilledMilitaryUnitCountDTO = new MilitaryUnitsKilledMilitaryUnitCountDTO(totalAttackPointForKillSoldierMainType, kiledMilitaryUnits);

        soldiers = soldiers.stream().filter(soldier -> soldier.getSoldierCount().compareTo(BigInteger.ZERO) > 0).toList();

        totalAttackPointForKillSoldierMainType.calculateTotalAttackPointForKillInfantrymanSubTypes(
                soldiers.stream().filter(soldier -> soldier instanceof Infantryman).toList());

        totalAttackPointForKillSoldierMainType.calculateTotalAttackPointForKillRifleSubTypes(
                soldiers.stream().filter(soldier -> soldier instanceof Rifle).toList());

        totalAttackPointForKillSoldierMainType.calculateTotalAttackPointForKillCannonSubTypes(
                soldiers.stream().filter(soldier -> soldier instanceof Cannon).toList());

        totalAttackPointForKillSoldierMainType.calculateTotalAttackPointForKillShipSubTypes(
                soldiers.stream().filter(soldier -> soldier instanceof Ship).toList());

        militaryUnitsKilledMilitaryUnitCountDTO = killSoldierWithAverageAttackPointOfEnemy(soldiers, militaryUnitsKilledMilitaryUnitCountDTO);

        return;
    }

    public void killAllSoldiers() {
        pikeman.setSoldierCount(BigInteger.ZERO);
        axeman.setSoldierCount(BigInteger.ZERO);
        archers.setSoldierCount(BigInteger.ZERO);
        swordsman.setSoldierCount(BigInteger.ZERO);
        lightArmedMusketeer.setSoldierCount(BigInteger.ZERO);
        mediumArmedMusketeer.setSoldierCount(BigInteger.ZERO);
        heavyArmedMusketeer.setSoldierCount(BigInteger.ZERO);
        culverin.setSoldierCount(BigInteger.ZERO);
        mortar.setSoldierCount(BigInteger.ZERO);
        ribault.setSoldierCount(BigInteger.ZERO);
        holk.setSoldierCount(BigInteger.ZERO);
        gunHolk.setSoldierCount(BigInteger.ZERO);
        carrack.setSoldierCount(BigInteger.ZERO);
    }
}
