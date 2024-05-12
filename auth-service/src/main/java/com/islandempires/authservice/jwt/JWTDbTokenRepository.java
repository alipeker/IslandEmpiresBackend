package com.islandempires.authservice.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface JWTDbTokenRepository extends JpaRepository<JWTDbToken, Long> {
    JWTDbToken findByToken(String token);
    List<JWTDbToken> findByActive(boolean active);

    @Query(value="SELECT * FROM JWTDB_TOKEN where sysdate - interval '60' minute > UPDATED_DATE", nativeQuery=true)
    List<JWTDbToken> findAllToDeleteTokens();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM JWTDB_TOKEN WHERE UPDATED_DATE < now() - interval '60' minute", nativeQuery = true)
    void deleteAllOlderTokens();
}