package com.islandempires.authservice.jwt;

import com.islandempires.authservice.exception.CustomException;
import com.islandempires.authservice.exception.ExceptionE;
import com.islandempires.authservice.model.AppUserRole;
import com.islandempires.authservice.security.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

  /**
   * THIS IS NOT A SECURE PRACTICE! For simplicity, we are storing a static key here. Ideally, in a
   * microservices environment, this key would be kept on a config-server.
   */
  @Value("${app.jwtSecret")
  private String secretKey;

  @Value("${app.jwtExpirationMs}")
  private long validityInMilliseconds = 360000000; // 1h

  @Autowired
  private MyUserDetails myUserDetails;

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String createToken(String username, Long userId, List<String> appUserRoles) {
    Claims claims = Jwts.claims().setSubject(username);
    claims.put("auth", appUserRoles.stream().map(s -> new SimpleGrantedAuthority(s)).filter(Objects::nonNull).collect(Collectors.toList()));
    claims.put("userId", userId.toString());
    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()//
        .setClaims(claims)//
        .setIssuedAt(now)//
        .setExpiration(validity)//
        .signWith(SignatureAlgorithm.HS256, secretKey)//
        .compact();
  }


  public Authentication getAuthentication(String token) {
    String username = getUsername(token);

    List<SimpleGrantedAuthority> roles = extractRoles(token);

    return new UsernamePasswordAuthenticationToken(
            username, null, roles);
  }

  private List<SimpleGrantedAuthority> extractRoles(String token) {
    Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    List<Object> rolesHashMapArray = claims.get("auth", List.class);
    if(0 == rolesHashMapArray.size()) {
      return new ArrayList<>();
    }
    try {
      LinkedHashMap<String, String> linkedHashMap = (LinkedHashMap<String, String>) rolesHashMapArray.get(0);
      return linkedHashMap
              .values()
              .stream()
              .map(Object::toString)
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toList());
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  public String getUsername(String token) {
    try {
      return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    } catch (Exception ex) {
      throw new CustomException(ExceptionE.TOKEN_EXPIRED);
    }
  }

  public String resolveToken(HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public String resolveToken(String bearerToken) {
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      throw new CustomException(ExceptionE.TOKEN_EXPIRED);
    }
  }

  public Long getUserId(String token) {
    try {
      Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
      return Long.parseLong(claims.get("userId", String.class));
    } catch (JwtException | IllegalArgumentException e) {
      throw new CustomException(ExceptionE.TOKEN_EXPIRED);
    }
  }

}
