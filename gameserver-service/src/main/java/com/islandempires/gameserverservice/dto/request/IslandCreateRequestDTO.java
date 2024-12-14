package com.islandempires.gameserverservice.dto.request;

import com.islandempires.gameserverservice.enums.CardinalDirectionsEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IslandCreateRequestDTO implements Serializable {

    private CardinalDirectionsEnum cardinalDirectionsEnum;

    private String islandName;

}
