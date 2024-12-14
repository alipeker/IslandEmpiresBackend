package com.islandempires.authservice.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class JwtResponse {
    @NonNull
    private String token;

    private String accessTokenType = "Bearer";

    private Long id;

    private List<String> appUserRoles;

    private String username;


    private String email;

}