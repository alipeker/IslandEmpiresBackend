package com.islandempires.militaryservice.dto;

import com.islandempires.militaryservice.model.soldier.Soldier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KillSoldierSubType {
    private Soldier soldier;
    private BigInteger totalKilledSoldier;
}
