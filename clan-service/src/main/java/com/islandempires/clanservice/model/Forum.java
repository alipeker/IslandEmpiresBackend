package com.islandempires.clanservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.islandempires.clanservice.exception.CustomRunTimeException;
import com.islandempires.clanservice.exception.ExceptionE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Forum",
        uniqueConstraints = @UniqueConstraint(columnNames = "clan_id"),
        indexes = @Index(name = "idx_clan_id", columnList = "clan_id"))
public class Forum implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Forum_generator")
    @SequenceGenerator(name="Forum_generator", sequenceName = "Forum_sequence", allocationSize=1)
    private Long id;

    @OneToMany(mappedBy = "forum", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("localDateTime ASC")
    private Set<ForumContent> forumContentList = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clan_id", nullable = false)
    @JsonBackReference
    @JsonIgnore
    private Clan clan;


    public ForumContent getForumContent(Long forumContentId) {
        return forumContentList.stream().filter(forumContent -> forumContent.getId().equals(forumContentId))
                .findFirst().orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));
    }

    public boolean deleteForumContent(Long forumContentId) {
        return forumContentList.removeIf(forumContent -> forumContent.getId().equals(forumContentId));
    }

    public void addForumContent(ForumContent newForumContent) {
        if(this.forumContentList.size() >= 30) {
            Optional<ForumContent> firstForumContent = forumContentList.stream().findFirst();
            firstForumContent.ifPresent(forumContentList::remove);
        }
        newForumContent.setForum(this);
        this.forumContentList.add(newForumContent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Forum forum = (Forum) o;
        return Objects.equals(id, forum.id) && Objects.equals(forumContentList, forum.forumContentList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, forumContentList);
    }

    @Override
    public String toString() {
        return "Forum{" +
                "id=" + id +
                ", forumContentList=" + forumContentList +
                '}';
    }
}
