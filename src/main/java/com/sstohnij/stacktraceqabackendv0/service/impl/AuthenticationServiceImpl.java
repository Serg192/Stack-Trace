package com.sstohnij.stacktraceqabackendv0.service.impl;

import com.sstohnij.stacktraceqabackendv0.config.token.TokenProps;
import com.sstohnij.stacktraceqabackendv0.dto.request.AuthenticationRequest;
import com.sstohnij.stacktraceqabackendv0.dto.request.RefreshTokenRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.AuthenticationResponse;
import com.sstohnij.stacktraceqabackendv0.entity.AppUser;
import com.sstohnij.stacktraceqabackendv0.repository.AppUserRepository;
import com.sstohnij.stacktraceqabackendv0.service.AuthenticationService;
import com.sstohnij.stacktraceqabackendv0.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AppUserRepository appUserRepository;
    private final TokenProps tokenProps;

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


        return AuthenticationResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        Jws<Claims> claimsJws = jwtService.validateToken(refreshToken);

        Claims body = claimsJws.getBody();
        AppUser appUser = appUserRepository.findByUsername((String) body.get("username")).get();

        String accessToken = jwtService.generateAccessToken(appUser);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String username) {
        //in progress
    }
}
