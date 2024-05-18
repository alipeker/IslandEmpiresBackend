package com.islandempires.sessiontrackingservice.cronjob;

import com.islandempires.sessiontrackingservice.service.IslandRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class IslandRecordCrontJob {

    @Autowired
    private IslandRecordService islandRecordService;

    @Scheduled(fixedRateString ="30000")
    public void islandRecordDeleteOldCronJob() {
        islandRecordService.removeAllOldIslandRecord();
    }

}
