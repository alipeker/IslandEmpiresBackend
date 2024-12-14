package com.islandempires.clanservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.islandempires.clanservice.enums.FriendRequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "idx_senderUser_id", columnList = "senderUser_id"),
        @Index(name = "idx_receiverUser_id", columnList = "receiverUser_id"),
        @Index(name = "idx_friendRequestStatus", columnList = "friendRequestStatus")
})
public class ServerUserFriend {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ServerUserFriend_generator")
    @SequenceGenerator(name="ServerUserFriend_generator", sequenceName = "ServerUserFriend_sequence", allocationSize=1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "senderUser_id", insertable = false, updatable = false)
    @JsonBackReference
    @JsonIgnore
    private ServerUser senderUser;

    @NonNull
    private Long senderUser_id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "receiverUser_id", insertable = false, updatable = false)
    @JsonBackReference
    @JsonIgnore
    private ServerUser receiverUser;

    @NonNull
    private Long receiverUser_id;

    @Enumerated(EnumType.STRING)
    @NonNull
    private FriendRequestStatus friendRequestStatus;

    @NonNull
    private LocalDateTime timestamp;

    public void acceptFriendRequest() {
        friendRequestStatus = FriendRequestStatus.ACCEPTED;
    }

    public void rejectFriendRequest() {
        friendRequestStatus = FriendRequestStatus.REJECTED;
    }

    public void pendingFriendRequest() {
        friendRequestStatus = FriendRequestStatus.PENDING;
    }
}
