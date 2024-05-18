package com.islandempires.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthanticateRequestDTO {
    String username;
    String password;
}
