package com.islandempires.militaryservice.dto;

import com.islandempires.militaryservice.model.MilitaryUnits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MilitaryUnitsKilledMilitaryUnitCountDTO {
    private TotalAttackPointForKillSoldierMainType totalAttackPointForKillSoldierMainType;
    private MilitaryUnits militaryUnits;

    public MilitaryUnitsKilledMilitaryUnitCountDTO cloneMilitaryUnit(MilitaryUnitsKilledMilitaryUnitCountDTO militaryUnitsKilledMilitaryUnitCountDTO) {
        try {
            militaryUnitsKilledMilitaryUnitCountDTO.setMilitaryUnits((MilitaryUnits) militaryUnits.clone());
            militaryUnitsKilledMilitaryUnitCountDTO.setTotalAttackPointForKillSoldierMainType(totalAttackPointForKillSoldierMainType);
            return militaryUnitsKilledMilitaryUnitCountDTO;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
