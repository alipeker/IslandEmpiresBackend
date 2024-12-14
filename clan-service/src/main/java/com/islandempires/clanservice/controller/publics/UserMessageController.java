package com.islandempires.clanservice.controller.publics;

import com.islandempires.clanservice.dto.ServerUserConversationMessageDTO;
import com.islandempires.clanservice.model.Conversation;
import com.islandempires.clanservice.service.UserConversationService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("clan/public/user/message")
@RequiredArgsConstructor
public class UserMessageController {

    @NonNull
    private final UserConversationService userConversationService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getServerUserAllConversations/{serverId}/{pageNumber}")
    private Page<Conversation> getServerUserAllConversationsPageable(@PathVariable String serverId, @PathVariable int pageNumber, @RequestAttribute("userId") Long userId) {
        return userConversationService.getServerUserAllConversationsPageable(serverId, userId, pageNumber);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getConversation/{conversationId}")
    private Conversation getConversation(@PathVariable Long conversationId, @RequestAttribute("userId") Long userId) {
        return userConversationService.getConversation(conversationId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/createConversation/{serverId}")
    private Long createConversation(@PathVariable String serverId, @RequestBody List<Long> participantIsList,
                                            @RequestAttribute("userId") Long userId) {
        return userConversationService.createConversation(serverId, participantIsList, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/sendMessage")
    private Conversation sendMessageInConversation(@Valid @RequestBody ServerUserConversationMessageDTO messageBody,
                                                   @RequestAttribute("userId") Long userId) {
        return userConversationService.sendMessageInConversation(messageBody.getConversationId(), messageBody.getMessageBody(), userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/deleteMessage/{messageId}")
    private void deleteMessageInConversation(@PathVariable  Long messageId,
                                                   @RequestAttribute("userId") Long userId) {
        userConversationService.deleteMessageInConversation(messageId, userId);
    }

}
