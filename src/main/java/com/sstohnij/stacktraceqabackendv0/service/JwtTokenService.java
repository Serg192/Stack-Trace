package com.sstohnij.stacktraceqabackendv0.service;

import com.sstohnij.stacktraceqabackendv0.entity.AppUser;
import com.sstohnij.stacktraceqabackendv0.entity.Token;
import com.sstohnij.stacktraceqabackendv0.enums.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.List;

public interface JwtTokenService {
    String generateAccessToken(AppUser appUser);
    String generateRefreshToken(AppUser appUser);
    String generateConfirmationToken(AppUser appUser);
    Jws<Claims> validateToken(String token);

    void validateIfRefreshTokenIsInWhitelist (AppUser user, String refreshToken);

    List<Token> findTokensByTypeAndUser(TokenType type, AppUser user);
    void saveToken(Token token);

    void removeRefreshToken(AppUser user, String token);
}
