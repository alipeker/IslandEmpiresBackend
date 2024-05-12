package com.islandempires.authservice.security;

import com.islandempires.authservice.exception.CustomException;
import com.islandempires.authservice.exception.ExceptionE;
import com.islandempires.authservice.jwt.JWTDbTokenService;
import com.islandempires.authservice.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MyUserDetails userDetailsService;

    @Autowired
    private JWTDbTokenService jwtDbTokenService;

    private HttpServletResponse prepareJwtException(HttpServletResponse response) throws IOException {
        CustomException errorResponse = new CustomException(ExceptionE.TOKEN_EXPIRED);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.setStatus(errorResponse.getStatus());
        String error = "{ \"statusCode\": " + errorResponse.getStatus() + ",\"date\" : \"" + new Date()
                + "\"," + "\"message\": \"" + errorResponse.getMessage() + "\"" + " }";
        response.getWriter().write(error);
        return response;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);

            if (jwt != null && jwtDbTokenService.isJWTDbTokenActive(jwt)) {
                String username = jwtTokenProvider.getUsername(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                jwtDbTokenService.updateTokenUpdatedDate(jwt);
            } else {
                if(jwt != null && !jwtTokenProvider.validateToken(jwt) && !request.getServletPath().equals("/auth/signin")
                        && !request.getServletPath().equals("/auth/signup")) {
                    prepareJwtException(response);
                    request.setAttribute("responseEntityExist", true);
                } else {
                    request.setAttribute("responseEntityExist", false);
                }

            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }

        return null;
    }
}
