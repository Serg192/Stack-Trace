package com.sstohnij.stacktraceqabackendv0.controller;

import com.sstohnij.stacktraceqabackendv0.common.ResponseObject;
import com.sstohnij.stacktraceqabackendv0.dto.request.UserRegistrationRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.UserResponse;
import com.sstohnij.stacktraceqabackendv0.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v0/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/new")
    public ResponseObject<UserResponse> registerUser(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest) {
      log.info("Received user registration request: {}", userRegistrationRequest);

      return ResponseObject.<UserResponse>builder()
              .status(ResponseObject.ResponseStatus.SUCCESSFUL)
              .message("User registered successfully")
              .data(userService.registerUser(userRegistrationRequest))
              .build();
    }
}
