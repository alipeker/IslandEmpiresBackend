package com.islandempires.resourcesservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IncreaseResourceRequest implements Serializable {
    @NotNull
    private Integer newHourlyResourceProduction;
}
