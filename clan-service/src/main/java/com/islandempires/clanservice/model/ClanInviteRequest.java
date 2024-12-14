package com.islandempires.clanservice.model;

import com.islandempires.clanservice.enums.ClanJoinRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clan_invite_requests",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"clan_id", "server_user_id"})},
        indexes = {
                @Index(name = "idx_clan_id", columnList = "clan_id"),
                @Index(name = "idx_server_user_id", columnList = "server_user_id")
        })
public class ClanInviteRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Clan_generator")
    @SequenceGenerator(name="Clan_generator", sequenceName = "Clan_sequence", allocationSize=1)
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

}
