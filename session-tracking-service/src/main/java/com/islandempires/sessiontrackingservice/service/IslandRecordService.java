package com.islandempires.sessiontrackingservice.service;

import com.islandempires.sessiontrackingservice.model.IslandRecord;
import com.islandempires.sessiontrackingservice.repository.IslandRecordRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class IslandRecordService {
    private IslandRecordRepository islandRecordRepository;

    public void saveIslandRecord(String islandId) {
        Optional<IslandRecord> islandRecordOptional = islandRecordRepository.findById(islandId);
        if(islandRecordOptional.isPresent()) {
            islandRecordOptional.get().setLocalDateTime(LocalDateTime.now());
            islandRecordRepository.save(islandRecordOptional.get());
            return;
        }
        IslandRecord islandRecord = new IslandRecord();
        islandRecord.setIslandId(islandId);
        islandRecord.setLocalDateTime(LocalDateTime.now());
        islandRecordRepository.save(islandRecord);
    }


    public void removeAllOldIslandRecord() {
        LocalDateTime sixtyMinutesAgo = LocalDateTime.now().minusMinutes(60);
        islandRecordRepository.deleteByLocalDateTimeBefore(sixtyMinutesAgo);
    }
}
