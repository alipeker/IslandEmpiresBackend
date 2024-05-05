package com.islandempires.gameserverservice.model;

import com.islandempires.gameserverservice.enums.OutboxEventType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Document("IslandOutboxEventRecord")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IslandOutboxEventRecord implements Serializable {

    @Id
    private String id;

    private OutboxEventType outboxEventType;

    private String islandId;

}
