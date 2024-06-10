package com.islandempires.militaryservice.dto;

import com.islandempires.militaryservice.model.soldier.Soldier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KillSoldierType {
    private List<Soldier> soldiers;
    private BigInteger totalAttackPointForKillSoldierType;
}
