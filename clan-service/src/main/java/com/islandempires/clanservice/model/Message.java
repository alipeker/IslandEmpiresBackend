package com.islandempires.clanservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Message_generator")
    @SequenceGenerator(name="Message_generator", sequenceName = "Message_sequence", allocationSize=1)
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private ServerUser sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @NonNull
    private String body;

    @NonNull
    private LocalDateTime timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id) && sender.equals(message.sender) && Objects.equals(conversation, message.conversation) && body.equals(message.body) && timestamp.equals(message.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sender, conversation, body, timestamp);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sender=" + sender +
                ", conversation=" + conversation +
                ", body='" + body + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
