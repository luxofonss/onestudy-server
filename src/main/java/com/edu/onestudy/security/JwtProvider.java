package com.edu.onestudy.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;

@Component
@Slf4j
public class JwtProvider {
    @Value("${security.jwt.secret}")
    private String secretKey;

    @Value("${security.jwt.access-token-expiration}")
    private long accessTokenExpired;

    @Value("${security.jwt.refresh-token-expiration}")
    private long refreshTokenExpired;

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.
                parserBuilder().
                setSigningKey(getSignInKey()).
                build().
                parseClaimsJws(token).
                getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(Authentication authentication) {
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        return generateToken(new HashMap<>(), userDetails, accessTokenExpired);
    }

    public String generateRefreshToken(Authentication authentication) {
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        return generateToken(new HashMap<>(), userDetails, refreshTokenExpired);
    }

    public String generateToken(
            Map<String, Object> extractClaims,
            UserPrincipal userDetails,
            long maxAge
    ) {

        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .claim("id", userDetails.getId())
                .claim("username", userDetails.getUsername())
                .claim("email", userDetails.getEmail())
                .claim("roles", userDetails.getRoles())
                .claim("authorities", userDetails.getAuthorities())
                .claim("scope", buildScope(userDetails))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + maxAge))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String buildScope(UserPrincipal user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role);
            });

        return stringJoiner.toString();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token, UserPrincipal userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
