package com.islandempires.gameserverservice.dto.island;

import com.islandempires.gameserverservice.enums.OutboxEventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IslandOutboxEventDTO {

    private Long id;

    private OutboxEventType outboxEventType;

    private String islandId;
}
