package com.islandempires.clanservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.islandempires.clanservice.dto.ForumMessageDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class ForumMessage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ForumMessage_generator")
    @SequenceGenerator(name="ForumMessage_generator", sequenceName = "ForumMessage_sequence", allocationSize=1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name="form_content_id")
    @JsonBackReference
    @JsonIgnore
    private ForumContent forumContent;

    @Size(max = 2000)
    @NonNull
    private String body;

    @OneToMany(mappedBy = "forumMessage", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Size(max = 10)
    private List<UrlWrapper> attachment = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "server_user_id")
    @JsonBackReference
    @JsonIgnore
    @NonNull
    private ServerUser serverUser;

    @NonNull
    private LocalDateTime localDateTime;

    @NonNull
    private LocalDateTime updatedLocalDateTime;

    public ForumMessage(ForumMessageDTO forumMessageDTO, ServerUser serverUser, ForumContent forumContent) {
        this.body = forumMessageDTO.getBody();
        this.attachment = forumMessageDTO.getAttachment();
        this.serverUser = serverUser;
        this.localDateTime = LocalDateTime.now();
        this.forumContent = forumContent;
    }

    public void setBody(String body) {
        this.body = body;
        this.updatedLocalDateTime = LocalDateTime.now();
    }

    public void update(ForumMessageDTO updateForumMessageDTO) {
        this.attachment = updateForumMessageDTO.getAttachment();
        this.body = updateForumMessageDTO.getBody();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForumMessage that = (ForumMessage) o;
        return Objects.equals(id, that.id) && body.equals(that.body) && localDateTime.equals(that.localDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, body, localDateTime);
    }

    @Override
    public String toString() {
        return "ForumMessage{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", localDateTime=" + localDateTime +
                '}';
    }
}
