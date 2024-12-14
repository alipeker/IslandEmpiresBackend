package com.islandempires.clanservice.service;

import com.islandempires.clanservice.dto.CreateForumContentDTO;
import com.islandempires.clanservice.dto.ForumMessageDTO;
import com.islandempires.clanservice.exception.CustomRunTimeException;
import com.islandempires.clanservice.exception.ExceptionE;
import com.islandempires.clanservice.model.*;
import com.islandempires.clanservice.repository.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClanForumService {
    @NonNull
    private final ForumRepository forumRepository;

    @NonNull
    private final ForumContentRepository forumContentRepository;

    @NonNull
    private final ForumMessageRepository forumMessageRepository;

    @NonNull
    private final ServerUserRepository serverUserRepository;

    @NonNull
    private final ClanRepository clanRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ForumContent createForumContent(CreateForumContentDTO createForumContentDTO, Long clanId, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndClanId(userId, clanId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        validateUserForumPrivileges(serverUser);

        ForumContent forumContent = new ForumContent(createForumContentDTO, serverUser, serverUser.getClan().getForum().getId());
        return forumContentRepository.save(forumContent);
    }

    private void validateUserForumPrivileges(ServerUser serverUser) {
        if (serverUser.getClanPrivileges() == null ||
                !serverUser.getClanPrivileges().isForumModeratorPrivileges()) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ForumContent editForumContent(CreateForumContentDTO createForumContentDTO, Long clanId, Long contentId, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndClanId(userId, clanId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        validateUserForumPrivileges(serverUser);

        ForumContent forumContent = forumContentRepository.findById(contentId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        ForumMessage forumMessage = forumContent.getForumMessage();

        forumContent.setHeader(createForumContentDTO.getHeader());
        forumMessage.update(createForumContentDTO.getForumMessage());

        return forumContentRepository.save(forumContent);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteForumContent(Long contentId, Long clanId, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndClanId(userId, clanId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        validateUserForumPrivileges(serverUser);

        ForumContent forumContent = forumContentRepository.findById(contentId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        if(!forumContent.getForum().getClan().getId().equals(clanId)) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }
        forumContentRepository.deleteById(forumContent.getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Page<ForumContent> getForumContentMessagesPageable(Long clanId, int pageNumber, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndClanId(userId, clanId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        Forum forum = forumRepository.findByClanId(clanId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

       return forumContentRepository.findByForumId(forum.getId(), Pageable.ofSize(10).withPage(pageNumber));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ForumContent getForumContent(Long forumContentId, Long clanId, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndClanId(userId, clanId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        return forumContentRepository.findById(forumContentId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendForumMessage(Long clanId, ForumMessageDTO forumMessageDTO, Long contentId, Long userId) {
        Clan clan = clanRepository.findById(clanId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        ServerUser serverUser = clan.getUserWithId(userId)
                                    .orElseThrow(() -> new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR));

        ForumContent forumContent = clan.getForum().getForumContent(contentId);
        forumContent.addForumMessage(forumMessageDTO, serverUser);
        forumContentRepository.save(forumContent);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void editForumMessage(Long clanId, Long contentId, Long forumMessageId, ForumMessageDTO forumMessageDTO, Long userId) {
        Clan clan = clanRepository.findById(clanId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        ServerUser serverUser = clan.getUserWithId(userId)
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR));

        ForumContent forumContent = clan.getForum().getForumContent(contentId);
        forumContent.updateForumMessage(forumMessageId, forumMessageDTO, serverUser);

        forumContentRepository.save(forumContent);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteForumMessage(Long clanId, Long forumMessageId, Long userId) {
        ServerUser serverUser = serverUserRepository.findByUserIdAndClanId(userId, clanId).orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        ForumMessage forumMessage = forumMessageRepository.findById(forumMessageId)
                                    .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        if(!forumMessage.getServerUser().equals(serverUser) && !serverUser.getClanPrivileges().isForumModeratorPrivileges()) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }

        forumMessageRepository.deleteById(forumMessage.getId());
    }
}
