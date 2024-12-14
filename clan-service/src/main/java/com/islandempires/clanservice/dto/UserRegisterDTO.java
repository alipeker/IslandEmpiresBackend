package com.islandempires.clanservice.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserRegisterDTO {

    private Long id;

    private String username;

    private String serverId;

}
