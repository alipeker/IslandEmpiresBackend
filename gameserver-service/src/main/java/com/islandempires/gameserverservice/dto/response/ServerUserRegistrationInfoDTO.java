package com.islandempires.gameserverservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerUserRegistrationInfoDTO {
    private String serverId;

    private boolean isUserRegister;

    private LocalDateTime registerDate;
}
