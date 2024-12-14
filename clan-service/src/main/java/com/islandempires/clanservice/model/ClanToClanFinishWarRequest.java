package com.islandempires.clanservice.model;

import com.islandempires.clanservice.enums.ClanToClanRequestStatus;
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
@Table(name = "clan_to_clan_finish_war_request",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"sender_clan_id", "receiver_clan_id"})},
        indexes = {@Index(name = "idx_sender_receiver_clan", columnList = "sender_clan_id, receiver_clan_id")})
public class ClanToClanFinishWarRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ClanToClanFinishWarRequest_generator")
    @SequenceGenerator(name="ClanToClanFinishWarRequest_generator", sequenceName = "ClanToClanFinishWarRequest_sequence", allocationSize=1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "sender_clan_id", nullable = false)
    private Clan senderClan;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "receiver_clan_id", nullable = false)
    private Clan receiverClan;

    private LocalDateTime requestDateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClanToClanFinishWarRequest that = (ClanToClanFinishWarRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(senderClan, that.senderClan) && Objects.equals(receiverClan, that.receiverClan) && Objects.equals(requestDateTime, that.requestDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, senderClan, receiverClan, requestDateTime);
    }

    @Override
    public String toString() {
        return "ClanToClanFinishWarRequest{" +
                "id=" + id +
                ", senderClan=" + senderClan +
                ", receiverClan=" + receiverClan +
                ", requestDateTime=" + requestDateTime +
                '}';
    }
}
