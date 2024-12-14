package com.islandempires.clanservice.controller.privates;

import com.islandempires.clanservice.dto.PublicClanDTO;
import com.islandempires.clanservice.dto.PublicUserDTO;
import com.islandempires.clanservice.dto.UserRegisterDTO;
import com.islandempires.clanservice.service.ClanService;
import com.islandempires.clanservice.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.islandempires.clanservice.dto.clan.ClanDTO;
import java.util.List;
import com.islandempires.clanservice.dto.user.UserPrivateDTO;

@CrossOrigin
@RestController
@RequestMapping("clan/private")
@RequiredArgsConstructor
public class ClanPrivateController {

    @NonNull
    private final UserService userService;

    @NonNull
    private final ClanService clanService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/registerUser")
    public void registerUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        userService.registerUser(userRegisterDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/joinClanAllowed/{serverId}/{userId}")
    public void joinClanAllowed(@PathVariable String serverId, @PathVariable Long userId) {
        userService.joinClanAllowed(serverId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/createClanAllowed/{serverId}/{userId}/{maxClanMemberNumber}")
    public void createClanAllowed(@PathVariable String serverId, @PathVariable Long userId, @PathVariable int maxClanMemberNumber) {
        userService.createClanAllowed(serverId, userId, maxClanMemberNumber);
    }


    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/setClanMaxMemberNumber/{serverId}/{userId}/{maxClanMemberNumber}")
    public void setClanMaxMemberNumber(@PathVariable String serverId, @PathVariable Long userId, @PathVariable int maxClanMemberNumber) {
        userService.setClanMaxMemberNumber(serverId, userId, maxClanMemberNumber);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/checkUsersSameClan/{serverId}/{userId1}/{userId2}")
    public Boolean checkUsersSameClan(@PathVariable String serverId, @PathVariable Long userId1, @PathVariable Long userId2) {
        return clanService.checkUsersSameClan(userId1, userId2, serverId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getClans")
    public List<PublicClanDTO> getClans() {
        return clanService.getClans();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getUsers")
    public List<PublicUserDTO> getUsers() {
        return clanService.getUsers();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getClan/{clanId}")
    public PublicClanDTO getClan(@PathVariable Long clanId) {
        return clanService.getClanPrivate(clanId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getUser/{userId}/{serverId}")
    public PublicUserDTO getUser(@PathVariable Long userId, @PathVariable String serverId) {
        return clanService.getUser(userId, serverId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getUserAll/{userId}/{serverId}")
    private UserPrivateDTO getServerUser(@PathVariable Long userId, @PathVariable String serverId) {
        return userService.getServerUserAll(userId, serverId);
    }
}
