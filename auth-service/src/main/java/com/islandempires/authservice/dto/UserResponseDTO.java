package com.islandempires.authservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserResponseDTO {

  private Long id;
  private String email;
  List<String> appUserRoles;

}
