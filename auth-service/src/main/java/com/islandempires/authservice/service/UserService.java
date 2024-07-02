package com.islandempires.authservice.service;

import com.islandempires.authservice.exception.CustomException;
import com.islandempires.authservice.exception.ExceptionE;
import com.islandempires.authservice.model.AppUser;
import com.islandempires.authservice.repository.UserRepository;
import com.islandempires.authservice.jwt.JwtResponse;
import com.islandempires.authservice.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;


    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return (UserDetails) userRepository.findByUsername(username);
            }
        };
    }

    public JwtResponse signin(String username, String password) {
        try {
            return authService.authenticateUser(username, password);
        } catch (AuthenticationException e) {
            throw new CustomException(ExceptionE.BAD_CREDENTIALS);
        }
    }

    public String signup(AppUser appUser) {
        if (!userRepository.existsByUsername(appUser.getUsername())) {
            appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
            userRepository.save(appUser);
            return jwtTokenProvider.createToken(appUser.getUsername(), appUser.getId(), appUser.getAppUserRoles());
        } else {
            throw new CustomException(ExceptionE.BAD_USERNAME);
        }
    }

    public void delete(String username) {
        userRepository.deleteByUsername(username);
    }

    public AppUser search(String username) {
        AppUser appUser = userRepository.findByUsername(username);
        if (appUser == null) {
            throw new CustomException(ExceptionE.USER_NOT_FOUND);
        }
        return appUser;
    }

    public Long whoami(String jwtToken) {
        return jwtTokenProvider.getUserId(jwtTokenProvider.resolveToken(jwtToken));
    }

    public boolean isTokenValid(String jwtToken) {
        return jwtTokenProvider.validateToken(jwtTokenProvider.resolveToken(jwtToken));
    }

    public List<AppUser> getAllUsers() {
        return this.userRepository.findAll();
    }


    @Override
    public void run(String... args) throws Exception {
        /*
        AppUser appUser = new AppUser();
        appUser.setEmail("admin@gmail.com");
        appUser.setUsername("admin");
        appUser.setPassword("admin");
        List<String> userRole = new ArrayList<>();
        userRole.add("1");
        appUser.setAppUserRoles(userRole);

        this.signup(appUser);*/
    }

}
