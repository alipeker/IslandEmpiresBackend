package com.islandempires.clanservice.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDescriptionDTO {

    private Long id;

    private String serverId;

    @Size(max = 1000)
    private String clanDescription;

}
