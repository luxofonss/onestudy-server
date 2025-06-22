package com.edu.onestudy.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    private static final String[] PUBLIC_URLS = {
            "/api/v1/pub/.*",
    };

    public JwtFilter(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestURI = request.getRequestURI();

        boolean isPublicUrl = Arrays.stream(PUBLIC_URLS)
                .anyMatch(requestURI::matches);

        if (isPublicUrl) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtProvider.extractUsername(jwt);
            } catch (ExpiredJwtException ex) {
                log.warn("JWT token is expired: {}", ex.getMessage());
            } catch (MalformedJwtException ex) {
                log.warn("JWT token is malformed: {}", ex.getMessage());
            } catch (SignatureException ex) {
                log.warn("JWT signature is invalid: {}", ex.getMessage());
            } catch (IllegalArgumentException ex) {
                log.warn("Unable to get JWT Token or JWT claims string is empty: {}", ex.getMessage());
            } catch (Exception ex) {
                log.error("An unexpected error occurred during JWT token processing: {}", ex.getMessage(), ex);
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserPrincipal userDetails = (UserPrincipal) this.userDetailsService.loadUserByUsername(username);

                if (jwtProvider.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    log.warn("Invalid JWT token for user: {}", username);
                }
            } catch (Exception ex) {
                log.error("Error authenticating user with JWT: {}", ex.getMessage(), ex);
            }
        }

        filterChain.doFilter(request, response);
    }
}