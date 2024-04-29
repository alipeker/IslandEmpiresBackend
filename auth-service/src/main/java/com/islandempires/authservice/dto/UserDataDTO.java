package com.islandempires.authservice.dto;

import com.islandempires.authservice.model.AppUserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDataDTO {

  private String username;
  private String email;
  private String password;
  List<AppUserRole> appUserRoles;

}
