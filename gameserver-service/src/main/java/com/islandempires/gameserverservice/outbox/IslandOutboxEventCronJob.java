package com.islandempires.gameserverservice.outbox;

import com.islandempires.gameserverservice.enums.OutboxEventType;
import com.islandempires.gameserverservice.kafka.KafkaOutboxProducerService;
import com.islandempires.gameserverservice.model.IslandOutboxEventRecord;
import com.islandempires.gameserverservice.repository.IslandOutboxEventRecordRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class IslandOutboxEventCronJob {

    private final IslandOutboxEventRecordRepository islandOutboxEventRepository;

    private final KafkaOutboxProducerService kafkaOutboxProducerService;

    private ModelMapper modelMapper;


    @Scheduled(fixedRateString ="30000")
    public void islandOutboxEventCronJob() {
        islandOutboxEventRepository.findAll()
                .flatMap(islandOutboxEvent -> {
                    if(islandOutboxEvent.getOutboxEventType().equals(OutboxEventType.DELETE)) {
                        return Mono.just(kafkaOutboxProducerService.sendDeleteIslandMessage(islandOutboxEvent.getIslandId()))
                                .then(islandOutboxEventRepository.delete(islandOutboxEvent));
                    }
                    return Mono.empty();
                })
                .subscribe();
    }

    public Flux<IslandOutboxEventRecord> sendKafka(IslandOutboxEventRecord islandOutboxEventRecord) {
        if(islandOutboxEventRecord.getOutboxEventType().equals(OutboxEventType.DELETE)) {
            kafkaOutboxProducerService.sendDeleteIslandMessage(islandOutboxEventRecord.getIslandId());
        }
        return Flux.just(islandOutboxEventRecord);
    }
}
