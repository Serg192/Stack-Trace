package com.sstohnij.stacktraceqabackendv0.service.impl;

import com.sstohnij.stacktraceqabackendv0.dto.request.AuthenticationRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.AuthenticationResponse;
import com.sstohnij.stacktraceqabackendv0.entity.AppUser;
import com.sstohnij.stacktraceqabackendv0.entity.Token;
import com.sstohnij.stacktraceqabackendv0.enums.TokenType;
import com.sstohnij.stacktraceqabackendv0.exception.custom.NotValidConformationTokenException;
import com.sstohnij.stacktraceqabackendv0.repository.AppUserRepository;
import com.sstohnij.stacktraceqabackendv0.service.AuthenticationService;
import com.sstohnij.stacktraceqabackendv0.service.JwtTokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtService;
    private final AppUserRepository appUserRepository;
    private final HttpServletResponse response;
    private final HttpServletRequest request;

    @Override
    @Transactional
    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        AppUser appUser = appUserRepository.findByUsername(authentication.getName()).get();
        String accessToken = jwtService.generateAccessToken(appUser);
        String refreshToken = jwtService.generateRefreshToken(appUser);

        log.info("SET OF ROLES: {}", appUser.getRoles());

        //Store refresh token in HttpOnly cookie
        Cookie refreshTokenCookie = new Cookie("jwt", refreshToken);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setHttpOnly(true);
        response.addCookie(refreshTokenCookie);

        //Save refresh token to database
        //Some kind of whitelist
        jwtService.saveToken(Token
                .builder()
                .tokenType(TokenType.REFRESH_TOKEN)
                .token(refreshToken)
                .user(appUser)
                .build());

        return AuthenticationResponse
                .builder()
                .accessToken(accessToken)
                .build();
    }

    @Override
    public AuthenticationResponse refreshToken() {

        String refreshToken = readRefreshTokenFromCookie();

        Jws<Claims> claimsJws = jwtService.validateToken(refreshToken);

        Claims body = claimsJws.getBody();
        AppUser appUser = appUserRepository.findByUsername((String) body.get("username")).get();

        jwtService.validateIfRefreshTokenIsInWhitelist(appUser, refreshToken);

        String accessToken = jwtService.generateAccessToken(appUser);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    @Override
    public void logout(String username) {
        AppUser user = appUserRepository.findByUsername(username).get();
        jwtService.removeRefreshToken(user, readRefreshTokenFromCookie());
        removeRefreshTokenFromCookie();
    }

    @Override
    public void confirmEmail(String token) {
        Jws<Claims> claims = jwtService.validateToken(token);

        if(claims == null)
            throw new NotValidConformationTokenException("Cannot verify email. Your verification link has expired");

        Claims body = claims.getBody();
        AppUser appUser = appUserRepository.findByUsername((String) body.get("username")).get();
        appUser.setEmailVerified(true);
        appUserRepository.save(appUser);
    }

    private String readRefreshTokenFromCookie(){

        final String refreshTokenNotFoundExceptionText = "Can't retrieve refresh token: cookie with the name 'jwt' not found";
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new AuthorizationServiceException(refreshTokenNotFoundExceptionText);
        }

        return Arrays.stream(cookies)
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new AuthorizationServiceException(refreshTokenNotFoundExceptionText));
    }

    private void removeRefreshTokenFromCookie(){
        Cookie refreshTokenCookie = new Cookie("jwt", null);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);
    }
}
