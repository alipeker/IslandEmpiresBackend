package com.islandempires.clanservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.islandempires.clanservice.dto.CreateForumContentDTO;
import com.islandempires.clanservice.dto.ForumMessageDTO;
import com.islandempires.clanservice.exception.CustomRunTimeException;
import com.islandempires.clanservice.exception.ExceptionE;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(indexes = @Index(name = "idx_forum_id", columnList = "forum_id"))
public class ForumContent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ForumContent_generator")
    @SequenceGenerator(name="ForumContent_generator", sequenceName = "ForumContent_sequence", allocationSize=1)
    private Long id;

    @OneToMany(mappedBy = "forumContent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("localDateTime ASC")
    private Set<ForumMessage> forumMessageList = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "forum_id", insertable = false, updatable = false)
    @JsonBackReference
    @JsonIgnore
    private Forum forum;

    private Long forum_id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "server_user_id")
    @NonNull
    private ServerUser serverUser;

    @Size(max = 400)
    @NonNull
    private String header;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @NonNull
    private ForumMessage forumMessage;

    private LocalDateTime localDateTime;

    private LocalDateTime updatedLocalDateTime;

    public ForumContent(CreateForumContentDTO createForumContentDTO, @NonNull ServerUser serverUser, Long forumId) {
        this.serverUser = serverUser;
        this.header = createForumContentDTO.getHeader();
        this.forumMessage = new ForumMessage(createForumContentDTO.getForumMessage(), serverUser, this);
        this.localDateTime = LocalDateTime.now();
        this.forum_id = forumId;
    }

    public void updateForumContent(CreateForumContentDTO createForumContentDTO) {
        this.header = createForumContentDTO.getHeader();
        this.forumMessage.setBody(createForumContentDTO.getForumMessage().getBody());
        this.forumMessage.setAttachment(createForumContentDTO.getForumMessage().getAttachment());
    }

    public void addForumMessage(ForumMessageDTO forumMessageDTO, ServerUser serverUser) {
        ForumMessage newForumMessage = new ForumMessage(forumMessageDTO, serverUser, this);
        forumMessageList.add(newForumMessage);
    }

    public void updateForumMessage(Long forumMessageId, ForumMessageDTO forumMessageDTO, ServerUser serverUser) {
        ForumMessage forumMessage = forumMessageList.stream().filter(forumMessageInList -> forumMessageInList.getId().equals(forumMessageId)).findFirst()
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
        if(!forumMessage.getServerUser().equals(serverUser) && !serverUser.getClanPrivileges().isForumModeratorPrivileges()) {
            throw new CustomRunTimeException(ExceptionE.PRIVILEGES_ERROR);
        }
        forumMessage.setBody(forumMessageDTO.getBody());
        forumMessage.setAttachment(forumMessageDTO.getAttachment());
        forumMessage.setServerUser(serverUser);
        forumMessage.setUpdatedLocalDateTime(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForumContent that = (ForumContent) o;
        return Objects.equals(id, that.id) && serverUser.equals(that.serverUser) && header.equals(that.header) && forumMessage.equals(that.forumMessage) && Objects.equals(localDateTime, that.localDateTime) && Objects.equals(updatedLocalDateTime, that.updatedLocalDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serverUser, header, forumMessage, localDateTime, updatedLocalDateTime);
    }

    @Override
    public String toString() {
        return "ForumContent{" +
                "id=" + id +
                ", header='" + header + '\'' +
                ", localDateTime=" + localDateTime +
                ", updatedLocalDateTime=" + updatedLocalDateTime +
                '}';
    }
}
