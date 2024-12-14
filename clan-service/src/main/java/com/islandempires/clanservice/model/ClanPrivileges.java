package com.islandempires.clanservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClanPrivileges implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ClanPrivileges_generator")
    @SequenceGenerator(name="ClanPrivileges_generator", sequenceName = "ClanPrivileges_sequence", allocationSize=1)
    private Long id;

    private Boolean admin = false;

    private Boolean invite = false;

    private Boolean diplomacy = false;

    private Boolean forumModerator = false;

    private Boolean secretForum = false;

    public void removeClanPrivileges() {
        admin = false;
        invite = false;
        diplomacy = false;
        forumModerator = false;
        secretForum = false;
    }

    public void controlIsAdminAndMakeChanges() {
        if(admin) {
            grantAdminPrivileges();
        }
    }


    public Boolean isAdminPrivileges() {
        return admin;
    }

    public Boolean isInvitePrivileges() {
        return invite;
    }

    public Boolean isDiplomacyPrivileges() {
        return diplomacy;
    }

    public Boolean isForumModeratorPrivileges() {
        return forumModerator;
    }

    public Boolean isSecretForumPrivileges() {
        return secretForum;
    }

    public ClanPrivileges grantAdminPrivileges() {
        admin = true;
        invite = true;
        diplomacy = true;
        forumModerator = true;
        secretForum = true;
        return this;
    }

    public void revokeAdminPrivileges() {
        admin = false;
    }

    public void grantInvitePrivileges() {
        invite = true;
    }

    public void revokeInvitePrivileges() {
        invite = false;
    }

    public void grantDiplomacyPrivileges() {
        diplomacy = true;
    }

    public void revokeDiplomacyPrivileges() {
        diplomacy = false;
    }

    public void grantForumModeratorPrivileges() {
        forumModerator = true;
    }

    public void revokeForumModeratorPrivileges() {
        forumModerator = false;
    }

    public void grantSecretForumPrivileges() {
        secretForum = true;
    }

    public void revokeSecretForumPrivileges() {
        secretForum = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClanPrivileges that = (ClanPrivileges) o;
        return Objects.equals(id, that.id) && Objects.equals(admin, that.admin) && Objects.equals(invite, that.invite) && Objects.equals(diplomacy, that.diplomacy) && Objects.equals(forumModerator, that.forumModerator) && Objects.equals(secretForum, that.secretForum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, admin, invite, diplomacy, forumModerator, secretForum);
    }

    @Override
    public String toString() {
        return "ClanPrivileges{" +
                "id=" + id +
                ", admin=" + admin +
                ", invite=" + invite +
                ", diplomacy=" + diplomacy +
                ", forumModerator=" + forumModerator +
                ", secretForum=" + secretForum +
                '}';
    }
}
