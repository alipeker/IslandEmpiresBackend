package com.islandempires.clanservice.model;

import com.islandempires.clanservice.enums.ClanJoinTypeEnum;
import com.islandempires.clanservice.enums.ClanToClanRequestStatus;
import com.islandempires.clanservice.exception.CustomRunTimeException;
import com.islandempires.clanservice.exception.ExceptionE;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "clan", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name"),
        @UniqueConstraint(columnNames = "shortName")
}, indexes = {
        @Index(name = "idx_clan_name", columnList = "name"),
        @Index(name = "idx_clan_shortName", columnList = "shortName")
})
public class Clan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Clan_generator")
    @SequenceGenerator(name="Clan_generator", sequenceName = "Clan_sequence", allocationSize=1)
    private Long id;

    @Size(min = 3, max = 30)
    private String name;

    @Size(min = 1, max = 7)
    private String shortName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "founder_server_user_id", insertable = false, updatable = false)
    private ServerUser founder;

    private Long founder_server_user_id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "server_id", updatable = false, insertable = false, nullable = false)
    private Server server;

    private String server_id;

    @OneToMany(mappedBy = "clan", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @OrderBy("clanJoinDateTime ASC")
    private Set<ServerUser> users = new HashSet<>();

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("requestLocalDateTime ASC")
    private Set<ClanJoinRequests> clanJoinRequests = new HashSet<>();

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("requestLocalDateTime ASC")
    private Set<ClanInviteRequest> clanInviteRequests = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "enemy_clans",
        joinColumns = @JoinColumn(name = "clan1_id"),
        inverseJoinColumns = @JoinColumn(name = "clan2_id")
    )
    private Set<Clan> enemyClans = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "friend_clans",
            joinColumns = @JoinColumn(name = "clan1_id"),
            inverseJoinColumns = @JoinColumn(name = "clan2_id")
    )
    private Set<Clan> friendClans = new HashSet<>();

    @OneToMany(mappedBy = "senderClan", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("requestDateTime ASC")
    private Set<ClanToClanFriendRequest> friendClanRequestsToClan = new HashSet<>();

    @OneToMany(mappedBy = "receiverClan", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("requestDateTime ASC")
    private Set<ClanToClanFriendRequest> friendClanRequestsFromClan = new HashSet<>();


    @OneToMany(mappedBy = "senderClan", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("requestDateTime ASC")
    private Set<ClanToClanFinishWarRequest> clanToClanFinishWarRequestFromClan = new HashSet<>();

    @OneToMany(mappedBy = "receiverClan", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("requestDateTime ASC")
    private Set<ClanToClanFinishWarRequest> clanToClanFinishWarRequestToClan = new HashSet<>();


    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("requestDateTime ASC")
    private Set<ReserveIsland> reserveIslands = new HashSet<>();

    private LocalDateTime createdDateTime;

    @Size(max = 1000)
    private String clanDescription;

    @ElementCollection
    @CollectionTable(name = "resource_attachments", joinColumns = @JoinColumn(name = "resource_id"))
    private List<UrlWrapper> attachments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ClanJoinTypeEnum clanJoinTypeEnum;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Forum forum;

    private int maxMemberNumber;

    public void addClanMember(ServerUser serverUser) {
        ClanPrivileges clanPrivileges = serverUser.getClanPrivileges();
        if(clanPrivileges != null) {
            clanPrivileges.removeClanPrivileges();
        } else {
            serverUser.initializeGrantPrivileges();
        }
        Hibernate.initialize(users);
        serverUser.setClan(this);
        users.add(serverUser);
    }

    public Boolean isUserAdmin(Long userId) {
        return users.stream().anyMatch(serverUser -> serverUser.getUser_id().equals(userId) && serverUser.isClanAdmin());
    }

    public Optional<ServerUser> getUserWithId(Long userId) {
        return users.stream().filter(user -> user.getUser_id().equals(userId)).findFirst();
    }

    public void addEnemyClan(Clan enemyClan) {
        if(this.enemyClans.contains(enemyClan) && enemyClan.getEnemyClans().contains(this)) {
            throw new CustomRunTimeException(ExceptionE.ALREADY_EXIST);
        }

        if(friendClans.contains(enemyClan)) {
            friendClans.remove(enemyClan);
            enemyClan.getFriendClans().remove(this);
        }

        friendClanRequestsToClan.removeIf(request ->
                request.getClanToClanRequestStatus().equals(ClanToClanRequestStatus.PENDING) &&
                        request.getSenderClan().getId().equals(enemyClan.getId())
        );

        friendClanRequestsFromClan.removeIf(request ->
                request.getClanToClanRequestStatus().equals(ClanToClanRequestStatus.PENDING) &&
                        request.getReceiverClan().getId().equals(enemyClan.getId())
        );

        if(!this.enemyClans.contains(enemyClan)) {
            this.enemyClans.add(enemyClan);
        }

        if(!enemyClan.getEnemyClans().contains(this)) {
            enemyClan.getEnemyClans().add(this);
        }
    }

    public void deleteEnemyClan(Clan enemyClan) {
        if(!this.enemyClans.contains(enemyClan)) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }
        this.enemyClans.remove(enemyClan);
    }


    public void addFriendClan(Clan friendClan) {
        if(this.friendClans.contains(friendClan) && friendClan.getFriendClans().contains(this)) {
            throw new CustomRunTimeException(ExceptionE.ALREADY_EXIST);
        }

        this.friendClanRequestsToClan.removeIf(friendClanRequestsToCLan -> friendClanRequestsToCLan.getSenderClan().equals(friendClan));

        if(!this.friendClans.contains(friendClan)) {
            this.friendClans.add(friendClan);
        }

        if(!friendClan.getFriendClans().contains(this)) {
            friendClan.getFriendClans().add(this);
        }
    }

    public void deleteFriendClan(Clan friendClan) {
        if(!this.friendClans.contains(friendClan) && !friendClan.getFriendClans().contains(this)) {
            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }
        this.friendClans.remove(friendClan);
        friendClan.getFriendClans().remove(this);
    }

    public void addFriendClanRequest(Clan friendClan) {
        if(friendClanRequestsToClan.stream().anyMatch(friendClanRequest -> friendClanRequest.getSenderClan().getId().equals(friendClan.getId()))) {
            friendClanRequestsToClan.removeIf(friendClanRequest -> friendClanRequest.getSenderClan().getId().equals(friendClan.getId()));
            addFriendClan(friendClan);
            return;
        }

        if(friendClanRequestsFromClan.stream().anyMatch(friendClanRequest -> friendClanRequest.getReceiverClan().getId().equals(friendClan.getId()))) {
            throw new CustomRunTimeException(ExceptionE.ALREADY_EXIST);
        }

        ClanToClanFriendRequest clanToClanFriendRequest = new ClanToClanFriendRequest(null, this, friendClan, ClanToClanRequestStatus.PENDING, LocalDateTime.now());
        friendClanRequestsFromClan.add(clanToClanFriendRequest);
    }

    public void deleteFriendClanRequest(Long requestId) {
        friendClanRequestsFromClan.removeIf(clanToClanFriendRequest -> clanToClanFriendRequest.getId().equals(requestId));
        friendClanRequestsToClan.removeIf(clanToClanFriendRequest -> clanToClanFriendRequest.getId().equals(requestId));
    }

    public ClanToClanFriendRequest getFriendClanRequest(Long requestId) {
        return friendClanRequestsToClan.stream().filter(clanToClanFriendRequest -> clanToClanFriendRequest.getId().equals(requestId))
                .findFirst().orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
    }

    public void addReserveIsland(ServerUser serverUser, String islandId) {
        if(reserveIslands.stream().anyMatch(reserveIsland -> reserveIsland.getIslandId().equals(islandId))) {
            throw new CustomRunTimeException(ExceptionE.ALREADY_EXIST);
        }

        reserveIslands.add(new ReserveIsland(null, this, islandId, serverUser, LocalDateTime.now()));
    }

    public void deleteReserveIsland(String islandId) {
        reserveIslands.removeIf(reserveIsland -> reserveIsland.getIslandId().equals(islandId));
    }

    public ReserveIsland getReserveIsland(String islandId) {
        return reserveIslands.stream().filter(reserveIsland -> reserveIsland.getIslandId().equals(islandId))
                .findFirst().orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
    }

    public boolean isMaxUserExceeded() {
        return maxMemberNumber == users.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clan clan = (Clan) o;
        return Objects.equals(id, clan.id) && Objects.equals(name, clan.name) && Objects.equals(shortName, clan.shortName) && Objects.equals(server_id, clan.server_id) && Objects.equals(createdDateTime, clan.createdDateTime) && clanJoinTypeEnum == clan.clanJoinTypeEnum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, shortName, server_id, createdDateTime, clanJoinTypeEnum);
    }

    @Override
    public String toString() {
        return "Clan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                '}';
    }


}
