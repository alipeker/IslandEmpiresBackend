package com.islandempires.authservice.security;

import com.islandempires.authservice.model.AppUserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
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