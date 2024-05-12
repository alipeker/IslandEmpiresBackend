package com.islandempires.authservice.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {

    private String token;
    private String accessTokenType = "Bearer";

    private Long id;

    private List<String> appUserRoles;

    private String username;


    private String email;

}