package com.sstohnij.stacktraceqabackendv0.controller;

import com.sstohnij.stacktraceqabackendv0.common.ResponseObject;
import com.sstohnij.stacktraceqabackendv0.dto.request.AuthenticationRequest;
import com.sstohnij.stacktraceqabackendv0.dto.request.RefreshTokenRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.AuthenticationResponse;
import com.sstohnij.stacktraceqabackendv0.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v0/auth")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Log in endpoint. Returns access token as a response and refresh token in http only cookie"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(authenticationService.login(request));
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Refresh token endpoint"
    )
    public ResponseEntity<AuthenticationResponse> refresh() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(authenticationService.refreshToken());
    }
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "Bearer Token")
    @Operation(
            summary = "Logout endpoint"
    )
    public ResponseObject<?> logout(Principal principal) {
        authenticationService.logout(principal.getName());

        return ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .message("User logged out")
                .build();
    }


    @GetMapping("/email-confirmation")
    @Operation(
            summary = "Email confirmation endpoint"
    )
    public ResponseEntity<?> confirmEmail(@RequestParam("token") String token) {
        authenticationService.confirmEmail(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
