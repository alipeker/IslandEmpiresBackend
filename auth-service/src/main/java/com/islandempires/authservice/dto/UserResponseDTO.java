package com.islandempires.authservice.dto;

import com.islandempires.authservice.model.AppUserRole;
import lombok.Data;

import java.util.List;

@Data
public class UserResponseDTO {

  private String username;
  private String email;
  List<AppUserRole> appUserRoles;

}
