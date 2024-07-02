package com.islandempires.militaryservice.schedule;

import com.islandempires.militaryservice.model.troopsAction.MovingTroops;
import com.islandempires.militaryservice.repository.MovingTroopsRepository;
import com.islandempires.militaryservice.repository.SoldierProductionRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final MovingTroopsRepository movingTroopsRepository;

    private final SoldierProductionRepository soldierProductionRepository;

    @Scheduled(fixedRateString = "10000")
    public void findComplatedScheduledJobs() {
        movingTroopsRepository.deleteInactiveMovingTroops();
    }

    @Scheduled(fixedRateString = "30000")
    @Transactional
    public void deleteSoldierProduction() {
        soldierProductionRepository.deleteInactive();
    }
}
