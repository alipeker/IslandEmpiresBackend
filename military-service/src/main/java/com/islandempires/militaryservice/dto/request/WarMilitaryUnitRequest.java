package com.islandempires.militaryservice.dto.request;

import com.islandempires.militaryservice.enums.MissionTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarMilitaryUnitRequest {

    private MissionTypeEnum missionType;

    private BigInteger pikeman = BigInteger.ZERO;

    private BigInteger axeman = BigInteger.ZERO;

    private BigInteger archers = BigInteger.ZERO;

    private BigInteger swordsman = BigInteger.ZERO;

    private BigInteger lightArmedMusketeer = BigInteger.ZERO;

    private BigInteger mediumArmedMusketeer = BigInteger.ZERO;

    private BigInteger heavyArmedMusketeer = BigInteger.ZERO;

    private BigInteger culverin = BigInteger.ZERO;

    private BigInteger mortar = BigInteger.ZERO;

    private BigInteger ribault = BigInteger.ZERO;

    private BigInteger holk = BigInteger.ZERO;

    private BigInteger gunHolk = BigInteger.ZERO;

    private BigInteger carrack = BigInteger.ZERO;

    private BigInteger missionary = BigInteger.ZERO;

    private BigInteger getTotalCannonCount() {
        return culverin.add(mortar).add(ribault).add(swordsman).add(lightArmedMusketeer).add(mediumArmedMusketeer).add(heavyArmedMusketeer);
    }

    private BigInteger getTotalInfantrymanAndRifleCount() {
        return  pikeman.add(axeman).add(archers).add(swordsman).add(lightArmedMusketeer).add(mediumArmedMusketeer).add(heavyArmedMusketeer);
    }
}
