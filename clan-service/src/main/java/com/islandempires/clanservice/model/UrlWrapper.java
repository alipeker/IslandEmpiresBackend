package com.islandempires.clanservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlWrapper {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UrlWrapper_generator")
    @SequenceGenerator(name="UrlWrapper_generator", sequenceName = "UrlWrapper_sequence", allocationSize=1)
    private Long id;

    @Pattern(regexp = "^(http|https)://.*$", message = "Invalid URL format")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "forum_message_id")
    private ForumMessage forumMessage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlWrapper that = (UrlWrapper) o;
        return Objects.equals(id, that.id) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url);
    }

    @Override
    public String toString() {
        return "UrlWrapper{" +
                "id=" + id +
                ", url='" + url + '\'' +
                '}';
    }
}
