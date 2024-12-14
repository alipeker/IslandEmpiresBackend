package com.islandempires.clanservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "userId"),
        @UniqueConstraint(columnNames = "username")
})
public class User implements Serializable {
    @Id
    @NonNull
    private Long userId;

    @NonNull
    private String username;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ServerUser> userServerList = new HashSet<>();

    @NonNull
    private LocalDateTime localDateTime;


    public void addUserToServerList(String serverId) {
        if(!controlUserServerExist(serverId)) {
            userServerList.add(new ServerUser(userId, serverId, LocalDateTime.now()));
        }
    }

    public Boolean controlUserServerExist(String serverId) {
        return userServerList.stream().anyMatch(userServer -> userServer.getServer().getId().equals(serverId));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId.equals(user.userId) && username.equals(user.username) && localDateTime.equals(user.localDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, localDateTime);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + userId +
                ", username='" + username + '\'' +
                ", localDateTime=" + localDateTime +
                '}';
    }
}
