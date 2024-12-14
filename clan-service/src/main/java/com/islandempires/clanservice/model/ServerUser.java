package com.islandempires.clanservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.islandempires.clanservice.dto.ClanPrivilegesDTO;
import com.islandempires.clanservice.dto.ClanPrivilegesSubDTO;
import com.islandempires.clanservice.exception.CustomRunTimeException;
import com.islandempires.clanservice.exception.ExceptionE;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "idx_serveruser_server_id", columnList = "server_id"),
        @Index(name = "idx_serveruser_user_id", columnList = "user_id")
})
public class ServerUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ServerUser_generator")
    @SequenceGenerator(name="ServerUser_generator", sequenceName = "ServerUser_sequence", allocationSize=1)
    private Long id;

    ServerUser(Long id) {
        this.id = id;
    }


    @JsonIgnore
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "clan_id")
    private Clan clan;

    @JsonIgnore
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;

    @NonNull
    private Long user_id;

    @JsonIgnore
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "server_id", updatable = false, insertable = false)
    private Server server;

    @NonNull
    private String server_id;

    private Boolean canUserJoinClan = true;

    private Boolean canUserCreateClan = false;

    private int maxClanMemberNumber = 0;

    @OneToMany(mappedBy = "senderUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ServerUserFriend> friendList = new HashSet<>();

    @OneToMany(mappedBy = "receiverUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ServerUserFriend> receiverList = new HashSet<>();

    @OneToMany(mappedBy = "serverUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("requestLocalDateTime ASC")
    private Set<ClanInviteRequest> clanInviteRequestSet = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "blocks",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<ServerUser> blockList = new HashSet<>();

    @ManyToMany(mappedBy = "participants", cascade = CascadeType.ALL)
    @OrderBy("lastMessageLocalDateTime DESC")
    private Set<Conversation> conversations = new HashSet<>();

    private LocalDateTime clanJoinDateTime;

    @NonNull
    private LocalDateTime userJoinLocalDateTime;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "privileges_id")
    private ClanPrivileges clanPrivileges;

    @OneToMany(mappedBy = "serverUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("requestLocalDateTime DESC")
    private Set<ClanJoinRequests> clanJoinRequests = new HashSet<>();

    public ServerUser initializeGrantPrivileges() {
        if(clanPrivileges == null) {
            setClanPrivileges(new ClanPrivileges(null, false, false, false, false, false));
        }
        return this;
    }

    public ServerUser initializeWithAdminGrantPrivileges() {
        if(clanPrivileges == null) {
            setClanPrivileges(new ClanPrivileges());
        }
        clanPrivileges.setAdmin(true);
        clanPrivileges.setInvite(true);
        clanPrivileges.setDiplomacy(true);
        clanPrivileges.setSecretForum(true);
        clanPrivileges.setForumModerator(true);
        return this;
    }

    public void removeClanPrivileges() {
        clanPrivileges.removeClanPrivileges();
    }

    public ServerUser grantAdminPrivileges() {
        clanPrivileges.grantAdminPrivileges();
        System.out.println("a");
        return this;
    }

    public void addBlockList(ServerUser blockServerUser) {
        if(blockList.stream().anyMatch(block -> block.getId().equals(blockServerUser.getId()))) {
            throw new CustomRunTimeException(ExceptionE.ALREADY_EXIST);
        }
        blockList.add(blockServerUser);
    }

    public void removeBlockList(Long blockServerUserId) {
        blockList.removeIf(block -> block.getId().equals(blockServerUserId));
    }

    public Boolean controlIsBlocked(Long userId) {
        return blockList.stream().anyMatch(block -> block.getUser_id().equals(userId));
    }

    public boolean isClanAdmin() {
        return clanPrivileges != null && clanPrivileges.getAdmin();
    }

    public void setClanPrivileges(ClanPrivilegesDTO clanPrivilegesDTOParent) {
        ClanPrivilegesSubDTO clanPrivilegesDTO = clanPrivilegesDTOParent.getClanPrivileges();
        if(clanPrivilegesDTO.getAdmin()) {
            clanPrivileges.setAdmin(true);
            clanPrivileges.setDiplomacy(true);
            clanPrivileges.setInvite(true);
            clanPrivileges.setSecretForum(true);
            clanPrivileges.setForumModerator(true);
        } else {
            clanPrivileges.setAdmin(false);
            clanPrivileges.setDiplomacy(clanPrivilegesDTO.getDiplomacy());
            clanPrivileges.setInvite(clanPrivilegesDTO.getInvite());
            clanPrivileges.setSecretForum(clanPrivilegesDTO.getSecretForum());
            clanPrivileges.setForumModerator(clanPrivilegesDTO.getForumModerator());
        }
    }

    public void setClanPrivileges(ClanPrivileges clanPrivileges) {
        this.clanPrivileges = clanPrivileges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerUser that = (ServerUser) o;
        return Objects.equals(id, that.id) && Objects.equals(userJoinLocalDateTime, that.userJoinLocalDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, blockList, clanJoinDateTime, userJoinLocalDateTime);
    }

    @Override
    public String toString() {
        return "ServerUser{" +
                "id=" + id +
                ", blockList=" + blockList +
                ", clanJoinDateTime=" + clanJoinDateTime +
                ", userJoinLocalDateTime=" + userJoinLocalDateTime +
                '}';
    }
}
