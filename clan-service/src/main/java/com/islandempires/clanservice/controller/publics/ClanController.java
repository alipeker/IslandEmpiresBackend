package com.islandempires.clanservice.controller.publics;

import com.islandempires.clanservice.dto.*;
import com.islandempires.clanservice.dto.clan.ClanDTO;
import com.islandempires.clanservice.dto.user.UserPrivateDTO;
import com.islandempires.clanservice.model.Clan;
import com.islandempires.clanservice.model.ClanPrivileges;
import com.islandempires.clanservice.service.ClanService;
import com.islandempires.clanservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.islandempires.clanservice.filter.client.WhoAmIClient;
import com.islandempires.clanservice.service.WebSocketBridgeService;
import com.islandempires.clanservice.outbox.KafkaProducerService;

import java.util.List;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("clan/public/clan")
public class ClanController {

    private final ClanService clanService;

    private final UserService userService;

    private final WhoAmIClient whoAmIClient;

    private final WebSocketBridgeService webSocketBridgeService;

    private final KafkaProducerService kafkaProducerService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{clanId}")
    public ClanDTO getClan(@RequestAttribute("userId") Long userId, @PathVariable Long clanId) {
        return clanService.getClan(clanId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/create")
    public UserPrivateDTO createClan(@RequestAttribute("userId") Long userId, @RequestBody CreateOrUpdateClanDTO createOrUpdateClanDTO) {
        Clan clan = clanService.createClan(createOrUpdateClanDTO, userId);
        UserPrivateDTO userPrivateDTO = userService.getServerUserAll(userId, createOrUpdateClanDTO.getServerId());
        try {
            webSocketBridgeService.sendClanChange(clan.getId(), userPrivateDTO.getClan());
            webSocketBridgeService.sendUserChange(createOrUpdateClanDTO.getServerId(), userId, userPrivateDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
       return userPrivateDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/update")
    public Clan updateClan(@RequestAttribute("userId") Long userId, @RequestBody CreateOrUpdateClanDTO createOrUpdateClanDTO) {
        return clanService.updateClan(createOrUpdateClanDTO, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/updateNameShortname")
    public ClanDTO updateNameShortname(@RequestAttribute("userId") Long userId, @RequestBody UpdateNameShortnameDTO updateNameShortnameDTO) {
        ClanDTO clanDTO = clanService.updateNameShortname(updateNameShortnameDTO, userId);

        try {
            webSocketBridgeService.sendClanChange(clanDTO.getId(), clanDTO);
            kafkaProducerService.sendClan(clanDTO.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return clanDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/updateDescription")
    public ClanDTO updateDescription(@RequestAttribute("userId") Long userId, @RequestBody UpdateDescriptionDTO updateDescriptionDTO) {
        ClanDTO clanDTO = clanService.updateDescription(updateDescriptionDTO, userId);
        
        try {
            webSocketBridgeService.sendClanChange(clanDTO.getId(), clanDTO);
            kafkaProducerService.sendClan(clanDTO.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return clanDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/updateJoinType")
    public ClanDTO updateJoinType(@RequestAttribute("userId") Long userId, @RequestBody UpdateClanJoinTypeDTO updateDescriptionDTO) {
        ClanDTO clanDTO = clanService.updateJoinType(updateDescriptionDTO, userId);
        
        try {
            webSocketBridgeService.sendClanChange(clanDTO.getId(), clanDTO);
            kafkaProducerService.sendClan(clanDTO.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return clanDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/delete/{clanId}")
    public void deleteClan(@RequestAttribute("userId") Long userId, @PathVariable Long clanId) {
        String serverId = clanService.deleteCLan(clanId, userId);
        UserPrivateDTO userPrivateDTO = userService.getServerUserAll(userId, serverId);
        try {
            webSocketBridgeService.sendDeleteClanEvent(clanId);
            webSocketBridgeService.deleteClanChange(clanId);
            webSocketBridgeService.sendUserChange(serverId, userId, userPrivateDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/leaveClan/{serverId}")
    public UserPrivateDTO leaveClan(@RequestAttribute("userId") Long userId, @PathVariable String serverId) {
        Long clanId = clanService.leaveClan(serverId, userId);

        UserPrivateDTO userPrivateDTO = userService.getServerUserAll(userId, serverId);
        
        try {
            ClanDTO clanDTO = clanService.getClanWithId(clanId);
            webSocketBridgeService.sendClanChange(clanDTO.getId(), clanDTO);
            webSocketBridgeService.sendUserChange(serverId, userId, userPrivateDTO);
            webSocketBridgeService.publishClanUserChange(serverId, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return userPrivateDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/inviteUser/{serverId}/{inviteUserId}")
    public ClanDTO inviteServerUser(@RequestAttribute("userId") Long userId, @PathVariable String serverId,
                                 @PathVariable Long inviteUserId) {
        Long clanId = clanService.inviteUser(serverId, inviteUserId, userId);

        ClanDTO clanDTO = clanService.getClanWithId(clanId);

        try {
            webSocketBridgeService.sendClanChange(clanDTO.getId(), clanDTO);
            UserPrivateDTO userPrivateDTO = userService.getServerUserAll(inviteUserId, serverId);
            webSocketBridgeService.sendUserChange(serverId, inviteUserId, userPrivateDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return clanDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/approveUserInviteRequest/{requestId}")
    public UserPrivateDTO approveServerUserInviteRequest(@RequestAttribute("userId") Long userId, @PathVariable Long requestId) {
        String serverId = clanService.approveServerUserInviteRequest(requestId, userId);
        UserPrivateDTO userPrivateDTO = userService.getServerUserAll(userId, serverId);
        try {
            ClanDTO clanDTO = userPrivateDTO.getClan();
            webSocketBridgeService.sendClanChange(clanDTO.getId(), clanDTO);
            webSocketBridgeService.sendUserChange(serverId, userId, userPrivateDTO);
            webSocketBridgeService.publishClanUserChange(serverId, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userPrivateDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/disapproveUserInviteRequest/{requestId}")
    public UserPrivateDTO disapproveServerUserInviteRequest(@RequestAttribute("userId") Long userId, @PathVariable Long requestId) {
        String serverId = clanService.disapproveServerUserInviteRequest(requestId, userId);
        UserPrivateDTO userPrivateDTO = userService.getServerUserAll(userId, serverId);
        try {
            ClanDTO clanDTO = userPrivateDTO.getClan();
            webSocketBridgeService.sendClanChange(clanDTO.getId(), clanDTO);
            webSocketBridgeService.sendUserChange(serverId, userId, userPrivateDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userPrivateDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/removeUserInviteRequest/{requestId}")
    public ClanDTO removeUserInviteRequest(@RequestAttribute("userId") Long userId, @PathVariable Long requestId) {
        Long clanId = clanService.removeUserInviteRequest(requestId, userId);
        ClanDTO clanDTO = clanService.getClanWithId(clanId);
        try {
            webSocketBridgeService.sendClanChange(clanDTO.getId(), clanDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clanDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/joinRequest/{clanId}")
    public UserPrivateDTO joinRequestServerUser(@RequestAttribute("userId") Long userId, @PathVariable Long clanId) {
        UserPrivateDTO userPrivateDTO = clanService.joinRequest(clanId, userId);

        try {
            ClanDTO clanDTO = userPrivateDTO.getClan();
            webSocketBridgeService.sendClanChange(clanDTO.getId(), clanDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userPrivateDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/approveUserJoinRequestClan/{requestId}")
    public ClanDTO approveServerUserJoinToClan(@RequestAttribute("userId") Long userId, @PathVariable Long requestId) {
        Long clanId = clanService.approveUserJoinClanRequest(requestId, userId);
        ClanDTO clanDTO = clanService.getClanWithId(clanId);

        try {
            webSocketBridgeService.sendClanChange(clanDTO.getId(), clanDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return clanDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/disapproveUserJoinRequestClan/{requestId}")
    public ClanDTO disapproveServerUserJoinToClan(@RequestAttribute("userId") Long userId, @PathVariable Long requestId) {
        Long clanId = clanService.disapproveUserJoinClanRequest(requestId, userId);
        ClanDTO clanDTO = clanService.getClanWithId(clanId);

        try {
            webSocketBridgeService.sendClanChange(clanDTO.getId(), clanDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return clanDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/kickUserFromClan/{clanId}/{kickUserId}")
    public ClanDTO kickUserFromClan(@RequestAttribute("userId") Long userId, @PathVariable Long clanId,
                                 @PathVariable Long kickUserId) {
        clanService.kickUserFromClan(clanId, kickUserId, userId);
        ClanDTO clanDTO = clanService.getClanWithId(clanId);

        try {
            webSocketBridgeService.sendClanChange(clanDTO.getId(), clanDTO);
            UserPrivateDTO userPrivateDTO = userService.getServerUserAll(kickUserId, clanDTO.getServerId());
            webSocketBridgeService.sendClanUserChange(clanDTO.getServerId(), kickUserId, userPrivateDTO);
            webSocketBridgeService.publishClanUserChange(clanDTO.getServerId(), kickUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return clanDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/addEnemyClan/{clanId}/{enemyClanId}")
    public ClanDTO addEnemyClan(@RequestAttribute("userId") Long userId, @PathVariable Long clanId,
                                 @PathVariable Long enemyClanId) {
        clanService.addEnemyClan(clanId, enemyClanId, userId);
        ClanDTO clanDTO = clanService.getClanWithId(clanId);

         try {
            ClanDTO enemyClanDTO = clanService.getClanWithId(enemyClanId);
            HashMap<Long, ClanDTO> clans = new HashMap<>();
            clans.put(clanDTO.getId(), clanDTO);
            clans.put(enemyClanDTO.getId(), enemyClanDTO);
            webSocketBridgeService.sendClansChange(clans);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return clanDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/finishWarClanRequest/{clanId}/{enemyClanId}")
    public ClanDTO finishWarClanRequest(@RequestAttribute("userId") Long userId, @PathVariable Long clanId,
                             @PathVariable Long enemyClanId) {
        clanService.finishWarClanRequest(clanId, enemyClanId, userId);
        ClanDTO clanDTO = clanService.getClanWithId(clanId);

         try {
            ClanDTO enemyClanDTO = clanService.getClanWithId(enemyClanId);
            HashMap<Long, ClanDTO> clans = new HashMap<>();
            clans.put(clanDTO.getId(), clanDTO);
            clans.put(enemyClanDTO.getId(), enemyClanDTO);
            webSocketBridgeService.sendClansChange(clans);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return clanDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/removeMyFinishWarRequest/{requestId}")
    public ClanDTO removeMyFinishWarRequest(@RequestAttribute("userId") Long userId, @PathVariable Long requestId) {
        List<Long> clanIdList = clanService.removeMyFinishWarRequest(requestId, userId);
        ClanDTO clanDTO = clanService.getClanWithId(clanIdList.get(0));
        try {
            ClanDTO enemyClanDTO = clanService.getClanWithId(clanIdList.get(1));
            HashMap<Long, ClanDTO> clans = new HashMap<>();
            clans.put(clanDTO.getId(), clanDTO);
            clans.put(enemyClanDTO.getId(), enemyClanDTO);
            webSocketBridgeService.sendClansChange(clans);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clanDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/approveRemoveEnemyClanRequest/{requestId}")
    public ClanDTO approveRemoveEnemyClanRequest(@RequestAttribute("userId") Long userId, @PathVariable Long requestId) {
        List<Long> clanIdList =  clanService.approveRemoveEnemyClanRequest(requestId, userId);
        ClanDTO clanDTO = clanService.getClanWithId(clanIdList.get(0));
        try {
            ClanDTO enemyClanDTO = clanService.getClanWithId(clanIdList.get(1));
            HashMap<Long, ClanDTO> clans = new HashMap<>();
            clans.put(clanDTO.getId(), clanDTO);
            clans.put(enemyClanDTO.getId(), enemyClanDTO);
            webSocketBridgeService.sendClansChange(clans);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clanDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/disapproveRemoveEnemyClanRequest/{requestId}")
    public ClanDTO disapproveRemoveEnemyClanRequest(@RequestAttribute("userId") Long userId, @PathVariable Long requestId) {
        List<Long> clanIdList = clanService.disapproveRemoveEnemyClanRequest(requestId, userId);
        ClanDTO clanDTO = clanService.getClanWithId(clanIdList.get(0));
        try {
            ClanDTO enemyClanDTO = clanService.getClanWithId(clanIdList.get(1));
            HashMap<Long, ClanDTO> clans = new HashMap<>();
            clans.put(clanDTO.getId(), clanDTO);
            clans.put(enemyClanDTO.getId(), enemyClanDTO);
            webSocketBridgeService.sendClansChange(clans);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clanDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/addFriendClanRequest/{clanId}/{friendClanId}")
    public ClanDTO addFriendClanRequest(@RequestAttribute("userId") Long userId, @PathVariable Long clanId,
                                        @PathVariable Long friendClanId) {
        clanService.addFriendClanRequest(clanId, friendClanId, userId);
        ClanDTO clanDTO = clanService.getClanWithId(clanId);

        try {
            ClanDTO enemyClanDTO = clanService.getClanWithId(friendClanId);
            HashMap<Long, ClanDTO> clans = new HashMap<>();
            clans.put(clanDTO.getId(), clanDTO);
            clans.put(enemyClanDTO.getId(), enemyClanDTO);
            webSocketBridgeService.sendClansChange(clans);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return clanDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/removeFriendClanRequest/{serverId}/{requestId}")
    public ClanDTO removeFriendClanRequest(@RequestAttribute("userId") Long userId, @PathVariable String serverId,
                                @PathVariable Long requestId) {
        List<Long> clanIdList = clanService.removeFriendClanRequest(requestId, serverId, userId);
        ClanDTO clanDTO = clanService.getClanWithId(clanIdList.get(0));

        try {
            ClanDTO friendClanDTO = clanService.getClanWithId(clanIdList.get(1));
            HashMap<Long, ClanDTO> clans = new HashMap<>();
            clans.put(clanDTO.getId(), clanDTO);
            clans.put(friendClanDTO.getId(), friendClanDTO);
            webSocketBridgeService.sendClansChange(clans);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return clanDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/approveFriendClanRequest/{requestId}")
    public ClanDTO approveFriendClanRequest(@RequestAttribute("userId") Long userId, @PathVariable Long requestId) {
        List<Long> clanIdList = clanService.approveFriendClanRequest(requestId, userId);
        ClanDTO clanDTO = clanService.getClanWithId(clanIdList.get(0));

        try {
            ClanDTO friendClanDTO = clanService.getClanWithId(clanIdList.get(1));
            HashMap<Long, ClanDTO> clans = new HashMap<>();
            clans.put(clanDTO.getId(), clanDTO);
            clans.put(friendClanDTO.getId(), friendClanDTO);
            webSocketBridgeService.sendClansChange(clans);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return clanDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/disapproveFriendClanRequest/{requestId}")
    public ClanDTO disapproveFriendClanRequest(@RequestAttribute("userId") Long userId, @PathVariable Long requestId) {
        List<Long> clanIdList = clanService.disapproveFriendClanRequest(requestId, userId);
        ClanDTO clanDTO = clanService.getClanWithId(clanIdList.get(0));

        try {
            ClanDTO friendClanDTO = clanService.getClanWithId(clanIdList.get(1));
            HashMap<Long, ClanDTO> clans = new HashMap<>();
            clans.put(clanDTO.getId(), clanDTO);
            clans.put(friendClanDTO.getId(), friendClanDTO);
            webSocketBridgeService.sendClansChange(clans);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return clanDTO;
    }


    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/removeFriendClan/{clanId}/{friendClanId}")
    public ClanDTO removeFriendClan(@RequestAttribute("userId") Long userId, @PathVariable Long clanId,
                                        @PathVariable Long friendClanId) {
        clanService.removeFriendClan(clanId, friendClanId, userId);
        ClanDTO clanDTO = clanService.getClanWithId(clanId);

        try {
            ClanDTO friendClanDTO = clanService.getClanWithId(friendClanId);
            HashMap<Long, ClanDTO> clans = new HashMap<>();
            clans.put(clanDTO.getId(), clanDTO);
            clans.put(friendClanDTO.getId(), friendClanDTO);
            webSocketBridgeService.sendClansChange(clans);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clanDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/changeGrantPrivileges/{serverId}/{memberUserId}")
    public void changeGrantPrivileges(@RequestAttribute("userId") Long userId,
                                 @PathVariable String serverId,
                                 @RequestBody ClanPrivileges clanPrivileges,
                                 @PathVariable Long memberUserId) {
        clanService.changeGrantPrivileges(memberUserId, clanPrivileges, serverId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/changeUsersGrantPrivileges/{serverId}/{memberUserId}")
    public ClanDTO changeUsersGrantPrivileges(@RequestAttribute("userId") Long userId,
                                           @PathVariable String serverId,
                                           @RequestBody List<ClanPrivilegesDTO> clanPrivilegesList,
                                           @PathVariable Long memberUserId) {
        ClanDTO clanDTO = clanService.changeUsersGrantPrivileges(memberUserId, clanPrivilegesList, serverId, userId);      
        try {
            webSocketBridgeService.sendClanChange(clanDTO.getId(), clanDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }  
        return clanDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reserveIsland/{serverId}/{islandId}")
    public ClanDTO reserveIsland(@RequestAttribute("userId") Long userId,
                                      @PathVariable String serverId,
                                      @PathVariable String islandId) {
        clanService.reserveIsland(serverId, islandId, userId);
        ClanDTO clanDTO = clanService.getClanWithUser(serverId, userId);
        
        try {
            webSocketBridgeService.sendClanChange(clanDTO.getId(), clanDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }  

        return clanDTO;
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/unReserveIsland/{serverId}/{islandId}")
    public ClanDTO unReserveIsland(@RequestAttribute("userId") Long userId,
                              @PathVariable String serverId,
                              @PathVariable String islandId) {
        clanService.unReserveIsland(serverId, islandId, userId);
        ClanDTO clanDTO = clanService.getClanWithUser(serverId, userId);
        
        try {
            webSocketBridgeService.sendClanChange(clanDTO.getId(), clanDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }  

        return clanDTO;
    }
}
