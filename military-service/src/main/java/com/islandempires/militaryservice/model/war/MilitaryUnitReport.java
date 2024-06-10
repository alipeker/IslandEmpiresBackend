package com.islandempires.militaryservice.model.war;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.islandempires.militaryservice.model.IslandMilitary;
import com.islandempires.militaryservice.model.MilitaryUnits;
import com.islandempires.militaryservice.model.soldier.cannon.Culverin;
import com.islandempires.militaryservice.model.soldier.cannon.Mortar;
import com.islandempires.militaryservice.model.soldier.cannon.Ribault;
import com.islandempires.militaryservice.model.soldier.infantryman.Archer;
import com.islandempires.militaryservice.model.soldier.infantryman.Axeman;
import com.islandempires.militaryservice.model.soldier.infantryman.Pikeman;
import com.islandempires.militaryservice.model.soldier.infantryman.Swordsman;
import com.islandempires.militaryservice.model.soldier.rifle.HeavyArmedMusketeer;
import com.islandempires.militaryservice.model.soldier.rifle.LightArmedMusketeer;
import com.islandempires.militaryservice.model.soldier.rifle.MediumArmedMusketeer;
import com.islandempires.militaryservice.model.soldier.ship.Carrack;
import com.islandempires.militaryservice.model.soldier.ship.GunHolk;
import com.islandempires.militaryservice.model.soldier.ship.Holk;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MilitaryUnitReport {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MilitaryUnitReport_generator")
    @SequenceGenerator(name="MilitaryUnitReport_generator", sequenceName = "MilitaryUnitReport_sequence", allocationSize=1)
    protected Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ownerId")
    @JsonBackReference
    private IslandMilitary owner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "defender_military_unit_report")
    private WarReport defenderMilitaryUnitReport;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "defender_military_unit_causalities_report")
    private WarReport defenderMilitaryUnitCausalitiesReport;

    private BigInteger pikeman;

    private BigInteger axeman;

    private BigInteger archers;

    private BigInteger swordsman;

    private BigInteger lightArmedMusketeer;

    private BigInteger mediumArmedMusketeer;

    private BigInteger heavyArmedMusketeer;

    private BigInteger culverin;

    private BigInteger mortar;

    private BigInteger ribault;

    private BigInteger holk;

    private BigInteger gunHolk;

    private BigInteger carrack;

    public void setMilitaryUnits(MilitaryUnits militaryUnit) {
        this.pikeman = militaryUnit.getPikeman().getSoldierCount();
        this.axeman = militaryUnit.getAxeman().getSoldierCount();
        this.archers = militaryUnit.getArchers().getSoldierCount();
        this.swordsman = militaryUnit.getSwordsman().getSoldierCount();
        this.lightArmedMusketeer = militaryUnit.getLightArmedMusketeer().getSoldierCount();
        this.mediumArmedMusketeer = militaryUnit.getMediumArmedMusketeer().getSoldierCount();
        this.heavyArmedMusketeer = militaryUnit.getHeavyArmedMusketeer().getSoldierCount();
        this.culverin = militaryUnit.getCulverin().getSoldierCount();
        this.mortar = militaryUnit.getMortar().getSoldierCount();
        this.ribault = militaryUnit.getRibault().getSoldierCount();
        this.holk = militaryUnit.getHolk().getSoldierCount();
        this.gunHolk = militaryUnit.getGunHolk().getSoldierCount();
        this.carrack = militaryUnit.getCarrack().getSoldierCount();
        this.owner = militaryUnit.getOwner();
    }
}
