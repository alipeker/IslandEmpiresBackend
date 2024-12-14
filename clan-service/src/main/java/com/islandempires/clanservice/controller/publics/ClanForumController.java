package com.islandempires.clanservice.controller.publics;

import com.islandempires.clanservice.dto.CreateForumContentDTO;
import com.islandempires.clanservice.dto.ForumMessageDTO;
import com.islandempires.clanservice.model.ForumContent;
import com.islandempires.clanservice.service.ClanForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("clan/public/forum")
public class ClanForumController {

    private final ClanForumService clanForumService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/forumContent/{clanId}/{forumId}")
    public ForumContent createForumContent(@RequestAttribute("userId") Long userId, @PathVariable Long clanId,
                                   @RequestBody CreateForumContentDTO createForumContentDTO) {
        return clanForumService.createForumContent(createForumContentDTO, clanId, userId);
    }


    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/forumContent/{clanId}/{contentId}")
    public ForumContent editForumContent(@RequestAttribute("userId") Long userId, @PathVariable Long clanId,
                                 @PathVariable Long contentId,
                                 @RequestBody CreateForumContentDTO createForumContentDTO) {
        return clanForumService.editForumContent(createForumContentDTO, clanId, contentId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/forumContent/{clanId}/{contentId}")
    public void deleteForumContent(@RequestAttribute("userId") Long userId, @PathVariable Long clanId,
                                 @PathVariable Long contentId) {
        clanForumService.deleteForumContent(clanId, contentId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/forumContents/{clanId}/{pageNumber}")
    public Page<ForumContent> getForumContentMessagesPageable(@RequestAttribute("userId") Long userId, @PathVariable Long clanId,
                                                              @PathVariable int pageNumber) {
        return clanForumService.getForumContentMessagesPageable(clanId, pageNumber, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/forumContents/{clanId}/{forumContentId}")
    public ForumContent getForumContent(@RequestAttribute("userId") Long userId,
                                        @PathVariable Long clanId,
                                        @PathVariable Long forumContentId) {
        return clanForumService.getForumContent(forumContentId, clanId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/forumMessage/{clanId}/{contentId}")
    public void sendMessageToForumContent(@RequestAttribute("userId") Long userId, @PathVariable Long clanId,
                                          @PathVariable Long contentId, @RequestBody ForumMessageDTO forumMessageDTO) {
        clanForumService.sendForumMessage(clanId, forumMessageDTO, contentId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/forumMessage/{clanId}/{contentId}/{forumMessageId}")
    public void editForumContentMessage(@RequestAttribute("userId") Long userId,
                                        @PathVariable Long clanId,
                                        @PathVariable Long contentId,
                                        @PathVariable Long forumMessageId,
                                        @RequestBody ForumMessageDTO forumMessageDTO) {
        clanForumService.editForumMessage(clanId, contentId, forumMessageId, forumMessageDTO, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/forumMessage/{clanId}/{forumMessageId}")
    public void deleteForumMessage(@RequestAttribute("userId") Long userId,
                                          @PathVariable Long clanId,
                                          @PathVariable Long forumMessageId) {
        clanForumService.deleteForumMessage(clanId, forumMessageId, userId);
    }

}
