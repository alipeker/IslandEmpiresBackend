package com.islandempires.clanservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Conversation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Conversation_generator")
    @SequenceGenerator(name="Conversation_generator", sequenceName = "Conversation_sequence", allocationSize=1)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "conversation_participants",
            joinColumns = @JoinColumn(name = "conversation_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<ServerUser> participants = new HashSet<>();

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("localDateTime ASC")
    @Size(max = 100)
    private Set<Message> messages = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_server_user_id", insertable = false, updatable = false)
    private ServerUser creatorServerUser;

    @NonNull
    private Long creator_server_user_id;

    @NonNull
    private LocalDateTime localDateTime;

    private LocalDateTime lastMessageLocalDateTime;

    public void addParticipants(List<ServerUser> serverUserList) {
        participants.addAll(serverUserList);
    }


    public void addMessage(Message message) {
        if(this.messages.size() >= 100) {
            this.messages.remove(0);
        }
        message.setTimestamp(LocalDateTime.now());
        this.messages.add(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return Objects.equals(id, that.id) && Objects.equals(participants, that.participants) && Objects.equals(messages, that.messages) && Objects.equals(creatorServerUser, that.creatorServerUser) && creator_server_user_id.equals(that.creator_server_user_id) && localDateTime.equals(that.localDateTime) && Objects.equals(lastMessageLocalDateTime, that.lastMessageLocalDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, participants, messages, creatorServerUser, creator_server_user_id, localDateTime, lastMessageLocalDateTime);
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                ", participants=" + participants +
                ", messages=" + messages +
                ", creatorServerUser=" + creatorServerUser +
                ", creator_server_user_id=" + creator_server_user_id +
                ", localDateTime=" + localDateTime +
                ", lastMessageLocalDateTime=" + lastMessageLocalDateTime +
                '}';
    }
}
