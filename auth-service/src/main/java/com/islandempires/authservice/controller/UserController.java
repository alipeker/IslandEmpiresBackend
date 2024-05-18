package com.islandempires.authservice.controller;

import com.islandempires.authservice.dto.UserDataDTO;
import com.islandempires.authservice.model.AppUser;
import com.islandempires.authservice.jwt.JwtResponse;
import com.islandempires.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class UserController {

  private final UserService userService;
  private final ModelMapper modelMapper;

  @PostMapping("/signin")
  public JwtResponse login(@RequestParam String username, @RequestParam String password) {
    return userService.signin(username, password);
  }

  @PostMapping("/signup")
  public String signup(@RequestBody UserDataDTO user) {
    return userService.signup(modelMapper.map(user, AppUser.class));
  }

  @GetMapping(value = "/me")
  public Long whoami(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
    return userService.whoami(jwtToken);
  }

  @GetMapping(value = "/isTokenValid")
  public boolean isTokenValid(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
    return userService.isTokenValid(jwtToken);
  }


}
