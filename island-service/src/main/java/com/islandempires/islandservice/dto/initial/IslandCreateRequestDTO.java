package com.islandempires.islandservice.dto.initial;

import com.islandempires.islandservice.enums.CardinalDirectionsEnum;
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
