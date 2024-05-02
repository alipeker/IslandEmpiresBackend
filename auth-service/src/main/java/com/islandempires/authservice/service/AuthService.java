package com.islandempires.authservice.service;

import com.islandempires.authservice.exception.CustomException;
import com.islandempires.authservice.exception.ExceptionE;
import com.islandempires.authservice.model.AppUser;
import com.islandempires.authservice.model.AppUserRole;
import com.islandempires.authservice.repository.UserRepository;
import com.islandempires.authservice.security.JwtResponse;
import com.islandempires.authservice.security.JwtTokenProvider;
import com.islandempires.authservice.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    MyUserDetails userDetailsServiceImpl;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    public JwtResponse authenticateUser(String userName, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userName, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userName);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority()).collect(Collectors.toList());
        String jwt = jwtTokenProvider.createToken(userName, roles);
        AppUser user = userRepository.findByUsername(userDetails.getUsername());
        if(user == null) {
            new CustomException(ExceptionE.USER_NOT_FOUND);
        }
        return new JwtResponse(jwt,
                "Bearer",
                user.getId(),
                user.getAppUserRoles(),
                user.getUsername(),
                user.getEmail());
    }

}
