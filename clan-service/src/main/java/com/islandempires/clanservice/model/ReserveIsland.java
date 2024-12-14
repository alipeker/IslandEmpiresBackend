package com.islandempires.clanservice.model;

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
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"clan_id", "islandId"})}
)
public class ReserveIsland implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ReserveIsland_generator")
    @SequenceGenerator(name="ReserveIsland_generator", sequenceName = "ReserveIsland_sequence", allocationSize=1)
    private Long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "clan_id", nullable = false)
    private Clan clan;

    private String islandId;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "server_user_id", nullable = false)
    private ServerUser serverUser;

    private LocalDateTime requestDateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReserveIsland that = (ReserveIsland) o;
        return Objects.equals(id, that.id) && Objects.equals(clan, that.clan) && Objects.equals(islandId, that.islandId) && Objects.equals(serverUser, that.serverUser) && Objects.equals(requestDateTime, that.requestDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clan, islandId, serverUser, requestDateTime);
    }

    @Override
    public String toString() {
        return "ReserveIsland{" +
                "id=" + id +
                ", clan=" + clan +
                ", islandId='" + islandId + '\'' +
                ", serverUser=" + serverUser +
                ", requestDateTime=" + requestDateTime +
                '}';
    }
}
