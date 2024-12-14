package com.islandempires.militaryservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.islandempires.militaryservice.dto.*;
import com.islandempires.militaryservice.dto.request.WarMilitaryUnitRequest;
import com.islandempires.militaryservice.enums.SoldierSubTypeEnum;
import com.islandempires.militaryservice.model.soldier.ShipBaseInfo;
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
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MilitaryUnits implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MilitaryUnits_generator")
    @SequenceGenerator(name="MilitaryUnits_generator", sequenceName = "MilitaryUnits_sequence", allocationSize=1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId")
    @JsonBackReference
    private IslandMilitary owner;

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
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Missionary missionary;

    public void initializeWithInitialValues(WarMilitaryUnitRequest warMilitaryUnitRequest) {
        pikeman.setSoldierCount(warMilitaryUnitRequest.getPikeman());
        axeman.setSoldierCount(warMilitaryUnitRequest.getAxeman());
        archers.setSoldierCount(warMilitaryUnitRequest.getArchers());
        swordsman.setSoldierCount(warMilitaryUnitRequest.getSwordsman());
        lightArmedMusketeer.setSoldierCount(warMilitaryUnitRequest.getLightArmedMusketeer());
        mediumArmedMusketeer.setSoldierCount(warMilitaryUnitRequest.getMediumArmedMusketeer());
        heavyArmedMusketeer.setSoldierCount(warMilitaryUnitRequest.getHeavyArmedMusketeer());
        culverin.setSoldierCount(warMilitaryUnitRequest.getCulverin());
        mortar.setSoldierCount(warMilitaryUnitRequest.getMortar());
        ribault.setSoldierCount(warMilitaryUnitRequest.getRibault());
        holk.setSoldierCount(warMilitaryUnitRequest.getHolk());
        gunHolk.setSoldierCount(warMilitaryUnitRequest.getGunHolk());
        carrack.setSoldierCount(warMilitaryUnitRequest.getCarrack());
        missionary.setSoldierCount(warMilitaryUnitRequest.getMissionary());
    }

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
                gameServerSoldier.getShipBaseInfoList().stream().filter(soldierBaseInfo -> soldierBaseInfo.getShipSubTypeName().equals(SoldierSubTypeEnum.HOLK.toString())).findFirst().orElseThrow(),
                gameServerSoldier.getSoldierBaseInfoList().stream().filter(soldierBaseInfo -> soldierBaseInfo.getSoldierSubTypeName().equals(SoldierSubTypeEnum.HOLK.toString())).findFirst().orElseThrow());
        this.gunHolk = createGunHolk(
                gameServerSoldier.getShipBaseInfoList().stream().filter(soldierBaseInfo -> soldierBaseInfo.getShipSubTypeName().equals(SoldierSubTypeEnum.GUN_HOLK.toString())).findFirst().orElseThrow(),
                gameServerSoldier.getSoldierBaseInfoList().stream().filter(soldierBaseInfo -> soldierBaseInfo.getSoldierSubTypeName().equals(SoldierSubTypeEnum.GUN_HOLK.toString())).findFirst().orElseThrow());
        this.carrack = createCarrack(
                gameServerSoldier.getShipBaseInfoList().stream().filter(soldierBaseInfo -> soldierBaseInfo.getShipSubTypeName().equals(SoldierSubTypeEnum.CARRACK.toString())).findFirst().orElseThrow(),
                gameServerSoldier.getSoldierBaseInfoList().stream().filter(soldierBaseInfo -> soldierBaseInfo.getSoldierSubTypeName().equals(SoldierSubTypeEnum.CARRACK.toString())).findFirst().orElseThrow());
        this.missionary = createMissionary(
                gameServerSoldier.getSoldierBaseInfoList().stream().filter(soldierBaseInfo -> soldierBaseInfo.getSoldierSubTypeName().equals(SoldierSubTypeEnum.MISSIONARY.toString())).findFirst().orElseThrow());
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

    private Holk createHolk(ShipBaseInfo shipBaseInfo, SoldierBaseInfo soldierBaseInfo) {
        Holk newHolk = new Holk();
        newHolk.setSoldierCount(BigInteger.ZERO);
        newHolk.setShipBaseInfo(shipBaseInfo);
        newHolk.setSoldierBaseInfo(soldierBaseInfo);
        return newHolk;
    }

    private GunHolk createGunHolk(ShipBaseInfo shipBaseInfo, SoldierBaseInfo soldierBaseInfo) {
        GunHolk newGunHolk = new GunHolk();
        newGunHolk.setSoldierCount(BigInteger.ZERO);
        newGunHolk.setShipBaseInfo(shipBaseInfo);
        newGunHolk.setSoldierBaseInfo(soldierBaseInfo);
        return newGunHolk;
    }

    private Carrack createCarrack(ShipBaseInfo shipBaseInfo, SoldierBaseInfo soldierBaseInfo) {
        Carrack newCarrack = new Carrack();
        newCarrack.setSoldierCount(BigInteger.ZERO);
        newCarrack.setShipBaseInfo(shipBaseInfo);
        newCarrack.setSoldierBaseInfo(soldierBaseInfo);
        return newCarrack;
    }

    private Missionary createMissionary(SoldierBaseInfo soldierBaseInfo) {
        Missionary newMissionary = new Missionary();
        newMissionary.setSoldierCount(BigInteger.ZERO);
        newMissionary.setSoldierBaseInfo(soldierBaseInfo);
        return newMissionary;
    }

    public BigInteger getInfantrymanNumber() {
        return swordsman.getSoldierCount()
                .add(pikeman.getSoldierCount())
                .add(archers.getSoldierCount())
                .add(axeman.getSoldierCount())
                .add(missionary.getSoldierCount());
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

    public List<Ship> getShips() {
        List<Ship> ships = new ArrayList<>();
        ships.add(holk);
        ships.add(gunHolk);
        ships.add(carrack);
        return ships;
    }

    public Soldier getSoldiersWithSoldierSubTypeEnum(SoldierSubTypeEnum soldierSubTypeEnum) {
        return prepareSoldierList().stream().filter(soldier -> soldier.getSoldierBaseInfo().getSoldierSubTypeName().equals(soldierSubTypeEnum.toString())).findFirst().orElseThrow();
    }

    public SoldierTotalDefenceAgainstSoldierType calculateTotalDefencePointPerEachSoldierType(double defenseMultiplier) {
        SoldierTotalDefenceAgainstSoldierType calculateTotalDefencePoints = new SoldierTotalDefenceAgainstSoldierType();
        List<SoldierTotalDefenceAgainstSoldierType> soldierTotalDefenceAgainstSoldierTypeList = new ArrayList<>();
        soldierTotalDefenceAgainstSoldierTypeList.add(pikeman.calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        soldierTotalDefenceAgainstSoldierTypeList.add(axeman.calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        soldierTotalDefenceAgainstSoldierTypeList.add(archers.calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        soldierTotalDefenceAgainstSoldierTypeList.add(swordsman.calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        soldierTotalDefenceAgainstSoldierTypeList.add(lightArmedMusketeer.calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        soldierTotalDefenceAgainstSoldierTypeList.add(mediumArmedMusketeer.calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        soldierTotalDefenceAgainstSoldierTypeList.add(heavyArmedMusketeer.calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        soldierTotalDefenceAgainstSoldierTypeList.add(culverin.calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        soldierTotalDefenceAgainstSoldierTypeList.add(mortar.calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        soldierTotalDefenceAgainstSoldierTypeList.add(ribault.calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        soldierTotalDefenceAgainstSoldierTypeList.add(holk.calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        soldierTotalDefenceAgainstSoldierTypeList.add(gunHolk.calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        soldierTotalDefenceAgainstSoldierTypeList.add(carrack.calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        soldierTotalDefenceAgainstSoldierTypeList.add(missionary.calculateTotalDefencePointsEachSoldierType(defenseMultiplier));
        return calculateTotalDefencePoints.addListPoints(soldierTotalDefenceAgainstSoldierTypeList);
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

    public BigDecimal calculateTotalDefencePointOfAllUnits(SoldierRatios soldierRatios, double defenseMultiplier) {
        BigDecimal totalDefencePoint = BigDecimal.ZERO;
        totalDefencePoint = totalDefencePoint.add(pikeman.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios,defenseMultiplier));
        totalDefencePoint = totalDefencePoint.add(axeman.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios, defenseMultiplier));
        totalDefencePoint = totalDefencePoint.add(archers.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios, defenseMultiplier));
        totalDefencePoint = totalDefencePoint.add(swordsman.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios, defenseMultiplier));
        totalDefencePoint = totalDefencePoint.add(lightArmedMusketeer.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios, defenseMultiplier));
        totalDefencePoint = totalDefencePoint.add(mediumArmedMusketeer.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios, defenseMultiplier));
        totalDefencePoint = totalDefencePoint.add(heavyArmedMusketeer.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios, defenseMultiplier));
        totalDefencePoint = totalDefencePoint.add(culverin.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios, defenseMultiplier));
        totalDefencePoint = totalDefencePoint.add(mortar.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios, defenseMultiplier));
        totalDefencePoint = totalDefencePoint.add(ribault.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios, defenseMultiplier));
        totalDefencePoint = totalDefencePoint.add(holk.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios, defenseMultiplier));
        totalDefencePoint = totalDefencePoint.add(gunHolk.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios, defenseMultiplier));
        totalDefencePoint = totalDefencePoint.add(carrack.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios, defenseMultiplier));
        totalDefencePoint = totalDefencePoint.add(missionary.calculateTotalDefencePointWithEnemySoldierRatios(soldierRatios, defenseMultiplier));
        return totalDefencePoint;
    }

    public BigInteger calculateTotalAttackPointOfAllUnits(double defenseMultiplier) {
        BigInteger totalAttackPoint = BigInteger.ZERO;

        totalAttackPoint = totalAttackPoint.add(pikeman.calculateTotalAttackPoint(defenseMultiplier));
        totalAttackPoint = totalAttackPoint.add(axeman.calculateTotalAttackPoint(defenseMultiplier));
        totalAttackPoint = totalAttackPoint.add(archers.calculateTotalAttackPoint(defenseMultiplier));
        totalAttackPoint = totalAttackPoint.add(swordsman.calculateTotalAttackPoint(defenseMultiplier));
        totalAttackPoint = totalAttackPoint.add(lightArmedMusketeer.calculateTotalAttackPoint(defenseMultiplier));
        totalAttackPoint = totalAttackPoint.add(mediumArmedMusketeer.calculateTotalAttackPoint(defenseMultiplier));
        totalAttackPoint = totalAttackPoint.add(heavyArmedMusketeer.calculateTotalAttackPoint(defenseMultiplier));
        totalAttackPoint = totalAttackPoint.add(culverin.calculateTotalAttackPoint(defenseMultiplier));
        totalAttackPoint = totalAttackPoint.add(mortar.calculateTotalAttackPoint(defenseMultiplier));
        totalAttackPoint = totalAttackPoint.add(ribault.calculateTotalAttackPoint(defenseMultiplier));
        totalAttackPoint = totalAttackPoint.add(holk.calculateTotalAttackPoint(defenseMultiplier));
        totalAttackPoint = totalAttackPoint.add(gunHolk.calculateTotalAttackPoint(defenseMultiplier));
        totalAttackPoint = totalAttackPoint.add(carrack.calculateTotalAttackPoint(defenseMultiplier));
        totalAttackPoint = totalAttackPoint.add(missionary.calculateTotalAttackPoint(defenseMultiplier));
        return totalAttackPoint;
    }

    public TotalAttackPointForKillSoldierMainType calculateTotalAttackPointPerEachOfMainSoldierType() {
        TotalAttackPointForKillSoldierMainType totalAttackPointForKillSoldierMainType = new TotalAttackPointForKillSoldierMainType();

        totalAttackPointForKillSoldierMainType = totalAttackPointForKillSoldierMainType.addInfantryManSubtype(SoldierSubTypeEnum.PIKEMAN, new BigDecimal(pikeman.calculateTotalAttackPoint(owner.getDefenceAndAttackMultiplier())));
        totalAttackPointForKillSoldierMainType = totalAttackPointForKillSoldierMainType.addInfantryManSubtype(SoldierSubTypeEnum.AXEMAN, new BigDecimal(axeman.calculateTotalAttackPoint(owner.getDefenceAndAttackMultiplier())));
        totalAttackPointForKillSoldierMainType = totalAttackPointForKillSoldierMainType.addInfantryManSubtype(SoldierSubTypeEnum.ARCHER, new BigDecimal(archers.calculateTotalAttackPoint(owner.getDefenceAndAttackMultiplier())));
        totalAttackPointForKillSoldierMainType = totalAttackPointForKillSoldierMainType.addInfantryManSubtype(SoldierSubTypeEnum.SWORDSMAN, new BigDecimal(swordsman.calculateTotalAttackPoint(owner.getDefenceAndAttackMultiplier())));

        totalAttackPointForKillSoldierMainType = totalAttackPointForKillSoldierMainType.addRifleSubtype(SoldierSubTypeEnum.LIGHT_ARMED_MUSKETEER, new BigDecimal(lightArmedMusketeer.calculateTotalAttackPoint(owner.getDefenceAndAttackMultiplier())));
        totalAttackPointForKillSoldierMainType = totalAttackPointForKillSoldierMainType.addRifleSubtype(SoldierSubTypeEnum.MEDIUM_ARMED_MUSKETEER, new BigDecimal(mediumArmedMusketeer.calculateTotalAttackPoint(owner.getDefenceAndAttackMultiplier())));
        totalAttackPointForKillSoldierMainType = totalAttackPointForKillSoldierMainType.addRifleSubtype(SoldierSubTypeEnum.HEAVY_ARMED_MUSKETEER, new BigDecimal(heavyArmedMusketeer.calculateTotalAttackPoint(owner.getDefenceAndAttackMultiplier())));

        totalAttackPointForKillSoldierMainType = totalAttackPointForKillSoldierMainType.addCannonSubtype(SoldierSubTypeEnum.CULVERIN, new BigDecimal(culverin.calculateTotalAttackPoint(owner.getDefenceAndAttackMultiplier())));
        totalAttackPointForKillSoldierMainType = totalAttackPointForKillSoldierMainType.addCannonSubtype(SoldierSubTypeEnum.MORTAR, new BigDecimal(mortar.calculateTotalAttackPoint(owner.getDefenceAndAttackMultiplier())));
        totalAttackPointForKillSoldierMainType = totalAttackPointForKillSoldierMainType.addCannonSubtype(SoldierSubTypeEnum.RIBAULT, new BigDecimal(ribault.calculateTotalAttackPoint(owner.getDefenceAndAttackMultiplier())));

        totalAttackPointForKillSoldierMainType = totalAttackPointForKillSoldierMainType.addShipSubtype(SoldierSubTypeEnum.HOLK, new BigDecimal(holk.calculateTotalAttackPoint(owner.getDefenceAndAttackMultiplier())));
        totalAttackPointForKillSoldierMainType = totalAttackPointForKillSoldierMainType.addShipSubtype(SoldierSubTypeEnum.GUN_HOLK, new BigDecimal(gunHolk.calculateTotalAttackPoint(owner.getDefenceAndAttackMultiplier())));
        totalAttackPointForKillSoldierMainType = totalAttackPointForKillSoldierMainType.addShipSubtype(SoldierSubTypeEnum.CARRACK, new BigDecimal(carrack.calculateTotalAttackPoint(owner.getDefenceAndAttackMultiplier())));

        totalAttackPointForKillSoldierMainType = totalAttackPointForKillSoldierMainType.addInfantryManSubtype(SoldierSubTypeEnum.MISSIONARY, new BigDecimal(missionary.calculateTotalAttackPoint(owner.getDefenceAndAttackMultiplier())));

        return totalAttackPointForKillSoldierMainType;
    }

    public TotalSoldierCount calculateTotalSoldierCount() {
        return new TotalSoldierCount(getInfantrymanNumber(), getRifleNumber(), getCannonNumber(), getShipNumber());
    }

    public void diminishingMilitaryUnitsCount(MilitaryUnits diminishingMilitaryUnits) {
        if(pikeman.getSoldierCount().compareTo(diminishingMilitaryUnits.getPikeman().getSoldierCount()) < 0) {
            throw new RuntimeException();
        }
        pikeman.setSoldierCount(pikeman.getSoldierCount().subtract(diminishingMilitaryUnits.getPikeman().getSoldierCount()));

        if(axeman.getSoldierCount().compareTo(diminishingMilitaryUnits.getAxeman().getSoldierCount()) < 0) {
            throw new RuntimeException();
        }
        axeman.setSoldierCount(axeman.getSoldierCount().subtract(diminishingMilitaryUnits.getAxeman().getSoldierCount()));

        if(archers.getSoldierCount().compareTo(diminishingMilitaryUnits.getArchers().getSoldierCount()) < 0) {
            throw new RuntimeException();
        }
        archers.setSoldierCount(archers.getSoldierCount().subtract(diminishingMilitaryUnits.getArchers().getSoldierCount()));

        if(swordsman.getSoldierCount().compareTo(diminishingMilitaryUnits.getSwordsman().getSoldierCount()) < 0) {
            throw new RuntimeException();
        }
        swordsman.setSoldierCount(swordsman.getSoldierCount().subtract(diminishingMilitaryUnits.getSwordsman().getSoldierCount()));

        if(lightArmedMusketeer.getSoldierCount().compareTo(diminishingMilitaryUnits.getLightArmedMusketeer().getSoldierCount()) < 0) {
            throw new RuntimeException();
        }
        lightArmedMusketeer.setSoldierCount(lightArmedMusketeer.getSoldierCount().subtract(diminishingMilitaryUnits.getLightArmedMusketeer().getSoldierCount()));

        if(mediumArmedMusketeer.getSoldierCount().compareTo(diminishingMilitaryUnits.getMediumArmedMusketeer().getSoldierCount()) < 0) {
            throw new RuntimeException();
        }
        mediumArmedMusketeer.setSoldierCount(mediumArmedMusketeer.getSoldierCount().subtract(diminishingMilitaryUnits.getMediumArmedMusketeer().getSoldierCount()));

        if(heavyArmedMusketeer.getSoldierCount().compareTo(diminishingMilitaryUnits.getHeavyArmedMusketeer().getSoldierCount()) < 0) {
            throw new RuntimeException();
        }
        heavyArmedMusketeer.setSoldierCount(heavyArmedMusketeer.getSoldierCount().subtract(diminishingMilitaryUnits.getHeavyArmedMusketeer().getSoldierCount()));

        if(culverin.getSoldierCount().compareTo(diminishingMilitaryUnits.getCulverin().getSoldierCount()) < 0) {
            throw new RuntimeException();
        }
        culverin.setSoldierCount(culverin.getSoldierCount().subtract(diminishingMilitaryUnits.getCulverin().getSoldierCount()));

        if(mortar.getSoldierCount().compareTo(diminishingMilitaryUnits.getMortar().getSoldierCount()) < 0) {
            throw new RuntimeException();
        }
        mortar.setSoldierCount(mortar.getSoldierCount().subtract(diminishingMilitaryUnits.getMortar().getSoldierCount()));

        if(ribault.getSoldierCount().compareTo(diminishingMilitaryUnits.getRibault().getSoldierCount()) < 0) {
            throw new RuntimeException();
        }
        ribault.setSoldierCount(ribault.getSoldierCount().subtract(diminishingMilitaryUnits.getRibault().getSoldierCount()));

        if(holk.getSoldierCount().compareTo(diminishingMilitaryUnits.getHolk().getSoldierCount()) < 0) {
            throw new RuntimeException();
        }
        holk.setSoldierCount(holk.getSoldierCount().subtract(diminishingMilitaryUnits.getHolk().getSoldierCount()));

        if(gunHolk.getSoldierCount().compareTo(diminishingMilitaryUnits.getGunHolk().getSoldierCount()) < 0) {
            throw new RuntimeException();
        }
        gunHolk.setSoldierCount(gunHolk.getSoldierCount().subtract(diminishingMilitaryUnits.getGunHolk().getSoldierCount()));

        if(carrack.getSoldierCount().compareTo(diminishingMilitaryUnits.getCarrack().getSoldierCount()) < 0) {
            throw new RuntimeException();
        }
        carrack.setSoldierCount(carrack.getSoldierCount().subtract(diminishingMilitaryUnits.getCarrack().getSoldierCount()));

        if(missionary.getSoldierCount().compareTo(diminishingMilitaryUnits.getMissionary().getSoldierCount()) < 0) {
            throw new RuntimeException();
        }
        missionary.setSoldierCount(missionary.getSoldierCount().subtract(diminishingMilitaryUnits.getMissionary().getSoldierCount()));
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
        missionary.setSoldierCount(missionary.getSoldierCount().add(addMilitaryUnits.getMissionary().getSoldierCount()));
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
        } else if(soldierTypeEnum.equals(SoldierSubTypeEnum.MISSIONARY)) {
            BigInteger totalSoldierCount = missionary.getSoldierCount().add(count).compareTo(BigInteger.ZERO) < 0
                    ? BigInteger.ZERO
                    : missionary.getSoldierCount().add(count);
            missionary.setSoldierCount(totalSoldierCount);
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

    /* Bu fonksiypna TotalAttackPointForKillSoldierMainType bu gelicek parametre SoldierTotalDefenceAgainstSoldierType bunun yerine
    *  Bu fonksiyon gelen SoldierTotalDefenceAgainstSoldierType değeri üzerinden her asker tipine göre kaç kişi ölmesi gerektiğini hesaplıyor
    *  Burada SoldierTotalDefenceAgainstSoldierType yerine direkt TotalAttackPointForKillSoldierMainType bunun gelmesini sağla
    *
    * */
    public MilitaryUnitsKilledMilitaryUnitCountDTO killSoldiersWithTotalStrengthDifferencePoint(SoldierTotalDefenceAgainstSoldierType enemySoldierTotalDefenceAgainstSoldierType,
                                                                                                BigDecimal killRatio, GameServerSoldier gameServerSoldier) {
        BigDecimal totalAttackPointForKillInfantryman = enemySoldierTotalDefenceAgainstSoldierType.getInfantrymanDefencePoint().divide(killRatio,10, RoundingMode.HALF_UP);
        BigDecimal totalAttackPointForKillRifle = enemySoldierTotalDefenceAgainstSoldierType.getRiflesDefencePoint().divide(killRatio,10, RoundingMode.HALF_UP);
        BigDecimal totalAttackPointForKillCannon = enemySoldierTotalDefenceAgainstSoldierType.getCannonDefencePoint().divide(killRatio,10, RoundingMode.HALF_UP);
        BigDecimal totalAttackPointForKillShip = enemySoldierTotalDefenceAgainstSoldierType.getShipDefencePoint().divide(killRatio,10, RoundingMode.HALF_UP);

        List<Soldier> soldiers = prepareSoldierList();

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
                soldiers.stream().filter(soldier -> soldier instanceof Infantryman).toList(), owner.getDefenceAndAttackMultiplier());

        totalAttackPointForKillSoldierMainType.calculateTotalAttackPointForKillRifleSubTypes(
                soldiers.stream().filter(soldier -> soldier instanceof Rifle).toList(), owner.getDefenceAndAttackMultiplier());

        totalAttackPointForKillSoldierMainType.calculateTotalAttackPointForKillCannonSubTypes(
                soldiers.stream().filter(soldier -> soldier instanceof Cannon).toList(), owner.getDefenceAndAttackMultiplier());

        totalAttackPointForKillSoldierMainType.calculateTotalAttackPointForKillShipSubTypes(
                soldiers.stream().filter(soldier -> soldier instanceof Ship).toList(), owner.getDefenceAndAttackMultiplier());

        militaryUnitsKilledMilitaryUnitCountDTO = killSoldierWithAverageAttackPointOfEnemy(soldiers, militaryUnitsKilledMilitaryUnitCountDTO);
        militaryUnitsKilledMilitaryUnitCountDTO.getMilitaryUnits().setOwner(getOwner());
        return militaryUnitsKilledMilitaryUnitCountDTO;
    }

    public MilitaryUnitsKilledMilitaryUnitCountDTO killSoldiersWithTotalStrengthDifferencePointDefenceWin(TotalAttackPointForKillSoldierMainType totalAttackPointForKillSoldierMainType2,
                                                                                                          SoldierTotalDefenceAgainstSoldierType soldierTotalDefenceAgainstSoldierType,
                                                                                                          GameServerSoldier gameServerSoldier) {

        List<Soldier> soldiers = prepareSoldierList();

        TotalAttackPointForKillSoldierMainType totalAttackPointForKillSoldierMainType = new TotalAttackPointForKillSoldierMainType();
        totalAttackPointForKillSoldierMainType.setTotalAttackPointForKillInfantryman(totalAttackPointForKillSoldierMainType2.getTotalAttackPointForKillInfantryman());
        totalAttackPointForKillSoldierMainType.setTotalAttackPointForKillRifle(totalAttackPointForKillSoldierMainType2.getTotalAttackPointForKillRifle());
        totalAttackPointForKillSoldierMainType.setTotalAttackPointForKillCannon(totalAttackPointForKillSoldierMainType2.getTotalAttackPointForKillCannon());
        totalAttackPointForKillSoldierMainType.setTotalAttackPointForKillShip(totalAttackPointForKillSoldierMainType2.getTotalAttackPointForKillShip());

        totalAttackPointForKillSoldierMainType.calculateTotalAttackPointForKillInfantrymanSubTypes(
                soldiers.stream().filter(soldier -> soldier instanceof Infantryman).toList(), owner.getDefenceAndAttackMultiplier());

        totalAttackPointForKillSoldierMainType.calculateTotalAttackPointForKillRifleSubTypes(
                soldiers.stream().filter(soldier -> soldier instanceof Rifle).toList(), owner.getDefenceAndAttackMultiplier());

        totalAttackPointForKillSoldierMainType.calculateTotalAttackPointForKillCannonSubTypes(
                soldiers.stream().filter(soldier -> soldier instanceof Cannon).toList(), owner.getDefenceAndAttackMultiplier());

        totalAttackPointForKillSoldierMainType.calculateTotalAttackPointForKillShipSubTypes(
                soldiers.stream().filter(soldier -> soldier instanceof Ship).toList(), owner.getDefenceAndAttackMultiplier());

        MilitaryUnits kiledMilitaryUnits = new MilitaryUnits();
        kiledMilitaryUnits.initialize(gameServerSoldier);
        MilitaryUnitsKilledMilitaryUnitCountDTO militaryUnitsKilledMilitaryUnitCountDTO = new MilitaryUnitsKilledMilitaryUnitCountDTO(totalAttackPointForKillSoldierMainType, kiledMilitaryUnits);

        return killSoldierWithAverageAttackPointOfEnemy(soldiers, militaryUnitsKilledMilitaryUnitCountDTO);
    }

    public List<Soldier> prepareSoldierList() {
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
        soldiers.add(missionary);
        return soldiers;
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
        missionary.setSoldierCount(BigInteger.ZERO);
    }

    public Duration findSlowerShipDuration() {
        return getShips().stream().filter(ship -> ship.getSoldierCount().compareTo(BigInteger.ZERO) > 0 && ship.getShipBaseInfo() != null && ship.getShipBaseInfo().getTimeToTraverseMapCell() != null)
                .max(Comparator.comparing(ship -> ship.getShipBaseInfo().getTimeToTraverseMapCell()))
                .orElseThrow().getShipBaseInfo().getTimeToTraverseMapCell();
    }

    public Boolean isShipCapacitySufficient() {
        BigInteger soldierCapacityOfShips = calculateTotalSoldierCapacityWithShips();
        BigInteger cannonCapacityOfShips = calculateTotalCannonCapacityWithShips();

        return soldierCapacityOfShips.compareTo(getTotalInfantrymanAndRifleCount()) >= 0 &&
                cannonCapacityOfShips.compareTo(getTotalCannonCount()) >= 0;
    }

    private BigInteger getTotalCannonCount() {
        return culverin.getSoldierCount().add(mortar.getSoldierCount()).add(ribault.getSoldierCount());
    }

    private BigInteger getTotalInfantrymanAndRifleCount() {
        return pikeman.getSoldierCount().add(axeman.getSoldierCount()).add(archers.getSoldierCount())
                .add(swordsman.getSoldierCount()).add(lightArmedMusketeer.getSoldierCount()).add(mediumArmedMusketeer.getSoldierCount())
                .add(heavyArmedMusketeer.getSoldierCount());
    }

    public BigInteger calculateTotalSoldierCapacityWithShips() {
        return getShips().stream()
                .map(ship -> {
                    ShipBaseInfo info = ship.getShipBaseInfo();
                    BigInteger soldierCapacity = BigInteger.valueOf(info.getSoldierCapacityOfShip());
                    BigInteger soldierCount = ship.getSoldierCount();
                    return soldierCapacity.multiply(soldierCount);
                })
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    public BigInteger calculateTotalCannonCapacityWithShips() {
        return getShips().stream()
                .map(ship -> {
                    ShipBaseInfo info = ship.getShipBaseInfo();
                    BigInteger soldierCapacity = BigInteger.valueOf(info.getCanonCapacityOfShip());
                    BigInteger soldierCount = ship.getSoldierCount();
                    return soldierCapacity.multiply(soldierCount);
                })
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    public BigInteger calculateTotalLootCapacityWithShips() {
        return getShips().stream()
                .map(Ship::getShipBaseInfo)
                .map(ShipBaseInfo::getTotalLootCapacity)
                .map(BigInteger::valueOf)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
