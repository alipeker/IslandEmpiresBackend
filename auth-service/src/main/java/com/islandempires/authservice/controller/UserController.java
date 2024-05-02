package com.islandempires.authservice.controller;

import com.islandempires.authservice.dto.UserDataDTO;
import com.islandempires.authservice.dto.UserResponseDTO;
import com.islandempires.authservice.model.AppUser;
import com.islandempires.authservice.security.JwtResponse;
import com.islandempires.authservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

  @PostMapping("/s")
  public String login(HttpServletRequest req) {
    return "a";
  }

  @PostMapping("/signup")
  public String signup(@RequestBody UserDataDTO user) {
    return userService.signup(modelMapper.map(user, AppUser.class));
  }

  public String delete(@PathVariable String username) {
    userService.delete(username);
    return username;
  }

  @GetMapping(value = "/me")
  public UserResponseDTO whoami(HttpServletRequest req) {
    return modelMapper.map(userService.whoami(req), UserResponseDTO.class);
  }


}
