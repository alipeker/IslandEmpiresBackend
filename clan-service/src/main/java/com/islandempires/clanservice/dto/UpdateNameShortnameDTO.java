package com.islandempires.clanservice.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNameShortnameDTO {

    private Long id;

    @Size(min = 3, max = 30)
    private String name;

    @Size(min = 1, max = 7)
    private String shortName;
    private String serverId;
}
