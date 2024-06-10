package com.islandempires.militaryservice.service;

import com.islandempires.militaryservice.model.MilitaryUnits;
import com.islandempires.militaryservice.model.troopsAction.MovingTroops;
import com.islandempires.militaryservice.model.war.AttackWarReport;
import com.islandempires.militaryservice.model.war.MilitaryUnitReport;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WarReportService {

    public AttackWarReport prepareAttackWarReportBeforeWar(MovingTroops movingTroops) {
        AttackWarReport attackWarReport = new AttackWarReport();

        MilitaryUnitReport attackerMilitaryUnitReport = new MilitaryUnitReport();
        attackerMilitaryUnitReport.setMilitaryUnits(movingTroops.getMilitaryUnits());

        MilitaryUnitReport defenderMilitaryUnitReport = new MilitaryUnitReport();
        defenderMilitaryUnitReport.setMilitaryUnits(movingTroops.getTargetToIslandMilitary().getStationaryTroops().getMilitaryUnits());

        List<MilitaryUnitReport> defenderSupportedMilitaryUnits = new ArrayList<>();

        movingTroops.getTargetToIslandMilitary().getAllMilitaryUnitsOnIsland().stream().forEach(supportedMilitaryUnits -> {
            MilitaryUnitReport defenderSupportedMilitaryUnitReport = new MilitaryUnitReport();
            defenderSupportedMilitaryUnitReport.setMilitaryUnits(supportedMilitaryUnits);
            defenderSupportedMilitaryUnits.add(defenderSupportedMilitaryUnitReport);
        });

        attackWarReport.setAttackerIslandMilitary(movingTroops.getOwnerIslandMilitary());
        attackWarReport.setDefenderIslandMilitary(movingTroops.getTargetToIslandMilitary());
        attackWarReport.setAttackerMilitaryUnitReport(attackerMilitaryUnitReport);
        attackWarReport.setDefenderMilitaryUnitReport(defenderSupportedMilitaryUnits);
        return attackWarReport;
    }

    public void prepareAndSaveAttackWarReportAfterWarAttackWin(AttackWarReport attackWarReport, MovingTroops movingTroops, MilitaryUnits attackerCasualtiesMilitaryUnit) {
        MilitaryUnitReport attackerCasualtiesReport = new MilitaryUnitReport();
        attackerCasualtiesMilitaryUnit.setOwner(movingTroops.getOwnerIslandMilitary());
        attackerCasualtiesReport.setMilitaryUnits(attackerCasualtiesMilitaryUnit);

        attackWarReport.setLocalDateTime(LocalDateTime.now());
        attackWarReport.setAttackerMilitaryUnitCausalitiesReport(attackerCasualtiesReport);
        attackWarReport.attackWin();
        System.out.println(attackWarReport);
    }

}
