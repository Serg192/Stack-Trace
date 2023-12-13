package com.sstohnij.stacktraceqabackendv0.service;

import com.sstohnij.stacktraceqabackendv0.entity.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public interface JwtService {
    String generateAccessToken(AppUser appUser);
    String generateRefreshToken(AppUser appUser);
    Jws<Claims> validateToken(String token);
}
