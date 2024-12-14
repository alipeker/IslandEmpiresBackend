package com.islandempires.authservice.controller;

import com.islandempires.authservice.dto.UserDTO;
import com.islandempires.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

import com.islandempires.authservice.model.AppUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth/private")
public class UserPrivateController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    public UserDTO toUserDTO(AppUser appUser) {
        return modelMapper.map(appUser, UserDTO.class);
    }

    @GetMapping(value = "/getAllUsers")
    public List<UserDTO> isTokenValid() {
        return userService.getAllUsers()
                .stream()
                .map(this::toUserDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/getUser/{userId}")
    public AppUser getUserWithUserId(@PathVariable Long userId) {
        return userService.getUserWithUserId(userId);
    }
}
