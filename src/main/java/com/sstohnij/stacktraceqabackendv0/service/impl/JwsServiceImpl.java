package com.sstohnij.stacktraceqabackendv0.service.impl;

import com.sstohnij.stacktraceqabackendv0.config.token.TokenProps;
import com.sstohnij.stacktraceqabackendv0.entity.AppUser;
import com.sstohnij.stacktraceqabackendv0.enums.TokenType;
import com.sstohnij.stacktraceqabackendv0.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.sstohnij.stacktraceqabackendv0.enums.TokenType.ACCESS_TOKEN;
import static com.sstohnij.stacktraceqabackendv0.enums.TokenType.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class JwsServiceImpl implements JwtService {

    private final TokenProps tokenProps;

    @Override
    public String generateAccessToken(AppUser appUser) {
        return generateToken(appUser, ACCESS_TOKEN);
    }

    @Override
    public String generateRefreshToken(AppUser appUser) {
        return generateToken(appUser, REFRESH_TOKEN);
    }

    @Override
    public Jws<Claims> validateToken(String token) {
        //if refresh check if it is in the database

        byte[] key = tokenProps.getSecretKey().getBytes();

        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(key))
                .build()
                .parseClaimsJws(token);
    }

    private String generateToken(AppUser appUser, TokenType tokenType){
        List<SimpleGrantedAuthority> authoritiesCollection = (List<SimpleGrantedAuthority>) appUser.getAuthorities();
        String authorities = authoritiesCollection.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        Map<String, Object> tokenBody = new HashMap<>();
        tokenBody.put("id", appUser.getId());
        tokenBody.put("username", appUser.getUsername());
        tokenBody.put("authoriities", authorities);

        byte[] key = tokenProps.getSecretKey().getBytes();
        final Long expirationInMin = tokenType == ACCESS_TOKEN ? tokenProps.getAccessTokenExpirationInMin() : tokenProps.getRefreshTokenExpirationInMin();
        final long expirationInMilliseconds = TimeUnit.MINUTES.toMillis(expirationInMin);

        try {
            return Jwts
                    .builder()
                    .addClaims(tokenBody)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setIssuer("self")
                    .setSubject(appUser.getUsername())
                    .setExpiration(new Date(System.currentTimeMillis() + expirationInMilliseconds))
                    .signWith(Keys.hmacShaKeyFor(key))
                    .compact();
        } catch (JwtException e) {
            throw new JwtException(e.getMessage());
        }
    }
}
