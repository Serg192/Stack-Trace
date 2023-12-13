package com.sstohnij.stacktraceqabackendv0.service;

import com.sstohnij.stacktraceqabackendv0.dto.request.AuthenticationRequest;
import com.sstohnij.stacktraceqabackendv0.dto.request.RefreshTokenRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest request);
    AuthenticationResponse refreshToken(RefreshTokenRequest request);

    void logout(String username);
}
