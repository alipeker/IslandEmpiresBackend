package com.islandempires.resourcesservice.dto.request;

import com.islandempires.resourcesservice.enums.IslandResourceEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IncreaseOrDecreaseIslandResourceFieldDTO implements Serializable {
    @NotNull
    private IslandResourceEnum islandResourceEnum;
    @NotNull
    private Number value;
}
