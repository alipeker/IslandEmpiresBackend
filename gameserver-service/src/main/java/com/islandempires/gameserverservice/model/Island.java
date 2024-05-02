package com.islandempires.gameserverservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;


@Data
@AllArgsConstructor
@EqualsAndHashCode
@RequiredArgsConstructor
public class Island implements Serializable {

    @Id
    private String id;

    private Long userId;

}
