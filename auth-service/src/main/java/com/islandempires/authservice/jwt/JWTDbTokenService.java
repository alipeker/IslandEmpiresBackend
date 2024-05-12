package com.islandempires.authservice.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
@EnableScheduling
public class JWTDbTokenService {

    @Autowired
    JWTDbTokenRepository jWTDbTokenRepository;

    @Autowired
    private  JwtTokenProvider jwtTokenProvider;

    public JWTDbToken recordToken(String token, Long kulNo) {
        JWTDbToken jwtDbToken = new JWTDbToken();
        jwtDbToken.setUserId(kulNo);
        jwtDbToken.setToken(token);
        jwtDbToken.setCreatedDate(new Date());
        jwtDbToken.setUpdatedDate(new Date());
        jwtDbToken.setType("Bearer");
        return jWTDbTokenRepository.save(jwtDbToken);
    }


    public void updateTokenUpdatedDate(String token) {
        JWTDbToken jWTDbToken = jWTDbTokenRepository.findByToken(token);
        if(jWTDbToken != null) {
            jWTDbToken.setUpdatedDate(new Date());
            jWTDbTokenRepository.save(jWTDbToken);
        }
    }

    public JWTDbToken getJWTDbToken(String token) {
        return jWTDbTokenRepository.findByToken(token);
    }

    public Long getJWTDbTokenUserId(String token) {
        return jWTDbTokenRepository.findByToken(jwtTokenProvider.resolveToken(token)).getUserId();
    }

    public boolean isJWTDbTokenActive(String token) {
        JWTDbToken jwtDbToken = jWTDbTokenRepository.findByToken(token);
        return jwtDbToken != null ? jwtDbToken.isActive() : false;
    }

    @Scheduled(fixedRateString ="PT10M")
    public void scheduleFixedRateTask() {
        jWTDbTokenRepository.deleteAllOlderTokens();
    }

}
