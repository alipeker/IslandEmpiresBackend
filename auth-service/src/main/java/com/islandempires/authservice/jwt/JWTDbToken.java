package com.islandempires.authservice.jwt;

import jakarta.persistence.*;
import lombok.*;


import java.util.Date;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "JWTDbToken")
@EqualsAndHashCode
public class JWTDbToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JWTDbToken_generator")
    @SequenceGenerator(name="JWTDbToken_generator", sequenceName = "JWTDbToken_sequence", allocationSize=1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String token;
    private String type;
    private Long userId;
    private Date createdDate = new Date();
    private Date updatedDate = new Date();
    private Date deathDate;
    private boolean active = true;

    public void deactive() {
        this.active = false;
        this.deathDate = new Date();
    }
}
