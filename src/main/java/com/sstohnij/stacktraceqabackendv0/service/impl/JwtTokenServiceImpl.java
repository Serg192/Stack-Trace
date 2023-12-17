package com.sstohnij.stacktraceqabackendv0.service.impl;

import com.sstohnij.stacktraceqabackendv0.config.token.TokenProps;
import com.sstohnij.stacktraceqabackendv0.entity.AppUser;
import com.sstohnij.stacktraceqabackendv0.entity.Token;
import com.sstohnij.stacktraceqabackendv0.enums.TokenType;
import com.sstohnij.stacktraceqabackendv0.repository.TokenRepository;
import com.sstohnij.stacktraceqabackendv0.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.sstohnij.stacktraceqabackendv0.enums.TokenType.*;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private final TokenProps tokenProps;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder encoder;
    @Override
    public String generateAccessToken(AppUser appUser) {
        return generateToken(appUser, ACCESS_TOKEN);
    }

    @Override
    public String generateRefreshToken(AppUser appUser) {
        return generateToken(appUser, REFRESH_TOKEN);
    }

    @Override
    public String generateConfirmationToken(AppUser appUser) {
        return generateToken(appUser, CONFIRMATION_TOKEN);
    }

    @Override
    public Jws<Claims> validateToken(String token) {

        byte[] key = tokenProps.getSecretKey().getBytes();

        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(key))
                .build()
                .parseClaimsJws(token);
    }

    @Override
    public void validateIfRefreshTokenIsInWhitelist(AppUser user, String refreshToken) {
        findRefreshTokenInDB(user, refreshToken).orElseThrow(() -> new JwtException("Refresh token is not valid"));
    }

    private String generateToken(AppUser appUser, TokenType tokenType){

        String authorities = appUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        Map<String, Object> tokenBody = new HashMap<>();
        tokenBody.put("id", appUser.getId());
        tokenBody.put("username", appUser.getUsername());
        tokenBody.put("authorities", authorities);

        byte[] key = tokenProps.getSecretKey().getBytes();
        final Long expirationInMin = getTokenExpiration(tokenType);
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


    @Override
    public List<Token> findTokensByTypeAndUser(TokenType type, AppUser user) {
        return tokenRepository.findTokensByTokenTypeAndUser(type, user);
    }

    @Override
    public void saveToken(Token token) {
        token.setToken(encoder.encode(token.getToken()));
        tokenRepository.save(token);
    }

    @Override
    public void removeRefreshToken(AppUser user, String token) {
        Optional<Token> refToken = findRefreshTokenInDB(user, token);
        refToken.ifPresent(tokenRepository::delete);
    }

    private Optional<Token> findRefreshTokenInDB(AppUser user, String refreshToken){
        return  tokenRepository.findTokensByTokenTypeAndUser(REFRESH_TOKEN, user)
                .stream()
                .filter(token -> encoder.matches(refreshToken, token.getToken()))
                .findFirst();
    }

    private Long getTokenExpiration(TokenType tokenType) {
        return switch (tokenType) {
            case ACCESS_TOKEN -> tokenProps.getAccessTokenExpirationInMin();
            case REFRESH_TOKEN -> tokenProps.getRefreshTokenExpirationInMin();
            case CONFIRMATION_TOKEN -> tokenProps.getConfirmationTokenExpiration();
        };
    }
}
