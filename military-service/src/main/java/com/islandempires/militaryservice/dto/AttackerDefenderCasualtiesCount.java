package com.islandempires.militaryservice.dto;


import com.islandempires.militaryservice.model.MilitaryUnits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttackerDefenderCasualtiesCount {
    private BigInteger attackerMilitaryUnitsCasualtiesCount;

    private BigInteger defenderMilitaryUnitsCasualtiesCount;
}

