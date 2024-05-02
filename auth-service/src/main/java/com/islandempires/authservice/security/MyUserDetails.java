package com.islandempires.authservice.security;

import com.islandempires.authservice.model.AppUser;
import com.islandempires.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyUserDetails implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    final AppUser appUser = userRepository.findByUsername(username);

    if (appUser == null) {
      throw new UsernameNotFoundException("User '" + username + "' not found");
    }
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(appUser.getAppUserRoles().toString()));

    return org.springframework.security.core.userdetails.User//
        .withUsername(username)//
        .password(appUser.getPassword())//
        .authorities(authorities)//
        .accountExpired(false)//
        .accountLocked(false)//
        .credentialsExpired(false)//
        .disabled(false)//
        .build();
  }

}
