package com.sstohnij.stacktraceqabackendv0.auth;

import com.sstohnij.stacktraceqabackendv0.service.JwtService;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenVerificationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = authorizationHeader.replace("Bearer ", "");

        try {
            Jws<Claims> claimsJws = jwtService.validateToken(accessToken);
            Claims body = claimsJws.getBody();
            String username = (String)body.get("username");
            String authorities = (String)body.get("authorities");

            if(username.isEmpty() || authorities.isEmpty()) {
                throw new MissingClaimException(claimsJws.getHeader(), body, "Token is invalid");
            }

            Set<SimpleGrantedAuthority> simpleGrantedAuthoritySet = Arrays.stream(authorities.split(" "))
                    .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    simpleGrantedAuthoritySet
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ExpiredJwtException e) {
            throw new JwtException("Token is expired");
        } catch (JwtException e) {
            throw new JwtException(e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
