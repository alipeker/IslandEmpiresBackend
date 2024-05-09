package com.islandempires.resourcesservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResourceAllocationRequestDTO implements Serializable {

    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer wood;

    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer clay;

    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer iron;

    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer population;

}
