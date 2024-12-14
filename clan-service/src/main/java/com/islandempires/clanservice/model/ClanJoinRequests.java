package com.islandempires.clanservice.model;

import com.islandempires.clanservice.enums.ClanJoinRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clan_join_requests",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"clan_id", "server_user_id"})},
        indexes = {
            @Index(name = "idx_clan_id", columnList = "clan_id"),
            @Index(name = "idx_server_user_id", columnList = "server_user_id")
})
public class ClanJoinRequests implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ClanJoinRequests_generator")
    @SequenceGenerator(name="ClanJoinRequests_generator", sequenceName = "ClanJoinRequests_sequence", allocationSize=1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "clan_id")
    private Clan clan;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "server_user_id")
    private ServerUser serverUser;

    @Enumerated(EnumType.STRING)
    private ClanJoinRequestStatus clanJoinRequestStatus;

    private LocalDateTime requestLocalDateTime;

    @Override
    public String toString() {
        return "ClanJoinRequests{" +
                "id=" + id +
                ", requestDateTime=" + requestLocalDateTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClanJoinRequests that = (ClanJoinRequests) o;
        return Objects.equals(id, that.id) && Objects.equals(requestLocalDateTime, that.requestLocalDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requestLocalDateTime);
    }
}
