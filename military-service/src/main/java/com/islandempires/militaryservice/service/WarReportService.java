package com.islandempires.militaryservice.service;

import com.islandempires.militaryservice.dto.MilitaryUnitsKilledMilitaryUnitCountDTO;
import com.islandempires.militaryservice.enums.MissionTypeEnum;
import com.islandempires.militaryservice.enums.WarWinnerEnum;
import com.islandempires.militaryservice.model.MilitaryUnits;
import com.islandempires.militaryservice.model.troopsAction.MovingTroops;
import com.islandempires.militaryservice.model.war.AttackWarReport;
import com.islandempires.militaryservice.model.war.MilitaryUnitReport;
import com.islandempires.militaryservice.model.war.WarReport;
import com.islandempires.militaryservice.repository.WarReportRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class WarReportService {

    private final WarReportRepository warReportRepository;

    public WarReport get(Long id, Long userId) {
        WarReport warReport = warReportRepository.findById(id).orElseThrow();
        if(!warReport.getAttackerIslandMilitary().getUserId().equals(userId) && !warReport.getDefenderIslandMilitary().getUserId().equals(userId)) {
            throw new RuntimeException();
        }
        return this.warReportRepository.findById(id).orElseThrow();
    }

    public WarReport get(Long id) {
        return warReportRepository.findById(id).orElseThrow();
    }

    public WarReport findUserServerWarReports(Long id) {
        return warReportRepository.findById(id).orElseThrow();
    }


    public AttackWarReport prepareAttackWarReportBeforeWar(MovingTroops movingTroops) {
        AttackWarReport attackWarReport = new AttackWarReport();

        attackWarReport.setMissionTypeEnum(movingTroops.getMissionType());

        MilitaryUnitReport attackerMilitaryUnitReport = new MilitaryUnitReport();
        attackerMilitaryUnitReport.setMilitaryUnits(movingTroops.getMilitaryUnits());

        List<MilitaryUnitReport> defenderSupportedMilitaryUnits = new ArrayList<>();
        movingTroops.getTargetToIslandMilitary().getAllMilitaryUnitsOnIsland().stream().forEach(supportedMilitaryUnits -> {
            MilitaryUnitReport defenderSupportedMilitaryUnitReport = new MilitaryUnitReport();
            defenderSupportedMilitaryUnitReport.setDefenderMilitaryUnitReport(attackWarReport);
            defenderSupportedMilitaryUnitReport.setMilitaryUnits(supportedMilitaryUnits);
            defenderSupportedMilitaryUnits.add(defenderSupportedMilitaryUnitReport);
        });

        attackWarReport.setAttacker_island_military_Id(movingTroops.getOwnerIslandMilitary().getIslandId());
        attackWarReport.setDefender_island_military_id(movingTroops.getTargetToIslandMilitary().getIslandId());
        attackWarReport.setAttackerMilitaryUnitReport(attackerMilitaryUnitReport);
        attackWarReport.setDefenderMilitaryUnitReport(defenderSupportedMilitaryUnits);
        return attackWarReport;
    }

    public void prepareAndSaveAttackWarReportAfterWarAttackWin(AttackWarReport attackWarReport, MilitaryUnits attackerCasualtiesMilitaryUnit) {
        MilitaryUnitReport attackerCasualtiesReport = new MilitaryUnitReport();
        attackerCasualtiesReport.setMilitaryUnits(attackerCasualtiesMilitaryUnit);
        attackWarReport.setAttackerMilitaryUnitCausalitiesReport(attackerCasualtiesReport);

        attackWarReport.setLocalDateTime(LocalDateTime.now());
        attackWarReport.attackWin(attackWarReport);
        attackWarReport.setWinner(WarWinnerEnum.ATTACK_WIN);
        warReportRepository.save(attackWarReport);
    }

    public void prepareAndSaveAttackWarReportAfterWarDefenceWin(AttackWarReport attackWarReport, List<MilitaryUnitsKilledMilitaryUnitCountDTO> militaryUnitsKilledMilitaryUnitCountDTOList) {
        List<MilitaryUnitReport> defenderCasulitiesReportList = new ArrayList<>();
        militaryUnitsKilledMilitaryUnitCountDTOList.forEach(militaryUnitsKilledMilitaryUnitCountDTO -> {
            MilitaryUnitReport defenderCasualtiesReport = new MilitaryUnitReport();
            defenderCasualtiesReport.setMilitaryUnits(militaryUnitsKilledMilitaryUnitCountDTO.getMilitaryUnits());
            defenderCasualtiesReport.setDefenderMilitaryUnitCausalitiesReport(attackWarReport);
            defenderCasulitiesReportList.add(defenderCasualtiesReport);
        });

        attackWarReport.setLocalDateTime(LocalDateTime.now());
        attackWarReport.setDefenderMilitaryUnitCausalitiesReport(defenderCasulitiesReportList);
        attackWarReport.defenceWin();
        attackWarReport.setWinner(WarWinnerEnum.DEFENCE_WIN);
        System.out.println(attackWarReport);
        warReportRepository.save(attackWarReport);
    }

}
