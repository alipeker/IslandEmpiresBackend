package com.islandempires.clanservice.filter;

import com.islandempires.clanservice.exception.CustomRunTimeException;
import com.islandempires.clanservice.exception.ExceptionE;
import com.islandempires.clanservice.filter.client.WhoAmIClient;
import com.islandempires.clanservice.repository.ServerUserRepository;
import feign.FeignException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FilterConfig extends OncePerRequestFilter {

    @NonNull
    private WhoAmIClient whoAmIClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getServletPath().startsWith("/clan/private")) {
            filterChain.doFilter(request, response);
            return;
        }
        /*
        request.setAttribute("userId", request.getHeader("userid"));
        filterChain.doFilter(request, response);*/

        String authorizationHeader = request.getHeader("Authorization");
        Long userId;
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                userId = whoAmIClient.whoami(authorizationHeader);
                request.setAttribute("userId", userId);
            } else {
                throw new CustomRunTimeException(ExceptionE.UNAUTHORIZED);
            }

            filterChain.doFilter(request, response);
        } catch (FeignException.Unauthorized e) {
            handleException(response, new CustomRunTimeException(ExceptionE.UNAUTHORIZED));
        } catch (CustomRunTimeException ex) {
            handleException(response, ex);
        }
    }

    private void handleException(HttpServletResponse response, CustomRunTimeException ex) throws IOException {
        response.sendError(ex.getHttpStatus().value(), ex.getMessage());
    }
}