package com.islandempires.clanservice.service;

import com.islandempires.clanservice.enums.FriendRequestStatus;
import com.islandempires.clanservice.exception.CustomRunTimeException;
import com.islandempires.clanservice.exception.ExceptionE;
import com.islandempires.clanservice.model.Conversation;
import com.islandempires.clanservice.model.Message;
import com.islandempires.clanservice.model.ServerUser;
import com.islandempires.clanservice.repository.ConversationRepository;
import com.islandempires.clanservice.repository.MessageRepository;
import com.islandempires.clanservice.repository.ServerUserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserConversationService {
    @NonNull
    private final ConversationRepository conversationRepository;

    @NonNull
    private final ServerUserRepository serverUserRepository;

    @NonNull
    private final MessageRepository messageRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Page<Conversation> getServerUserAllConversationsPageable(String serverId, Long userId, int pageNumber) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(!serverUser.getUser_id().equals(userId)) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        return conversationRepository.findAllByParticipant(serverUser.getId(), PageRequest.of(pageNumber, 10));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Conversation getConversation(Long conversationId, Long userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(conversation.getParticipants().stream().noneMatch(participant -> participant.getUser() != null &&
                                                            participant.getUser().getUserId().equals(userId))) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        return conversation;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Boolean controlAllParticipantIsFriend(ServerUser serverUser, List<Long> participantIdList) {
        return participantIdList.stream().allMatch(participantId ->
                serverUser.getFriendList().stream().anyMatch(friend ->
                        friend.getFriendRequestStatus().equals(FriendRequestStatus.ACCEPTED) &&
                                (friend.getSenderUser().getUser_id().equals(participantId) ||
                                        friend.getReceiverUser().getUser_id().equals(participantId))
                )
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long createConversation(String serverId, List<Long> participantIdList, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndServerId(userId, serverId)
                                 .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(!controlAllParticipantIsFriend(serverUser, participantIdList)) {
            throw new CustomRunTimeException(ExceptionE.PARTICIPANT_IS_NOT_FRIEND);
        }

        participantIdList.add(serverUser.getId());
        Optional<Conversation> alreadyExistConversation = conversationRepository.findByParticipants(participantIdList, participantIdList.size());
        if(alreadyExistConversation.isPresent()) {
            return alreadyExistConversation.get().getId();
        }

        Conversation conversation = new Conversation(serverUser.getId(), LocalDateTime.now());

        List<ServerUser> participantServerUserList = serverUserRepository.findAllById(participantIdList);

        List<Long> fetchedIds = participantServerUserList.stream()
                .map(ServerUser::getId)
                .toList();

        List<Long> missingIds = participantIdList.stream()
                .filter(id -> !fetchedIds.contains(id))
                .toList();

        if(participantServerUserList.stream().noneMatch(participant -> participant.getUser().getUserId().equals(userId))) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }
        if (!missingIds.isEmpty()) {
            throw new CustomRunTimeException(ExceptionE.PARTICIPANT_NOT_FOUND, missingIds);
        }

        conversation.addParticipants(participantServerUserList);
        conversation.setLastMessageLocalDateTime(LocalDateTime.now());

        return conversationRepository.save(conversation).getId();

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Conversation sendMessageInConversation(Long conversationId, String messageBody, Long userId) {
        Optional<Conversation> conversation = conversationRepository.findById(conversationId);

        if(conversation.isEmpty()) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }

        Optional<ServerUser> serverUserOptional = conversation.get()
                                                    .getParticipants().stream()
                                                    .filter(participant -> participant.getUser_id().equals(userId))
                                                    .findFirst();

        if(serverUserOptional.isEmpty()) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        conversation.get().addMessage(new Message(serverUserOptional.get(), messageBody, LocalDateTime.now()));
        conversation.get().setLastMessageLocalDateTime(LocalDateTime.now());
        return conversationRepository.save(conversation.get());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteMessageInConversation(Long messageId, Long userId) {
        Optional<Message> message = messageRepository.findById(messageId);

        if(message.isEmpty()) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }

        if(!message.get().getSender().getUser_id().equals(userId)) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        messageRepository.deleteById(message.get().getId());
    }
}
