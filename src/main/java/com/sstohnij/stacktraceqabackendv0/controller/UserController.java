package com.sstohnij.stacktraceqabackendv0.controller;

import com.sstohnij.stacktraceqabackendv0.common.ResponseObject;
import com.sstohnij.stacktraceqabackendv0.dto.request.UserRegistrationRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.UserResponse;
import com.sstohnij.stacktraceqabackendv0.dto.response.UsersPageResponse;
import com.sstohnij.stacktraceqabackendv0.entity.AppUser;
import com.sstohnij.stacktraceqabackendv0.mapper.UserMapper;
import com.sstohnij.stacktraceqabackendv0.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v0/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users")
public class UserController {

    private final UserService userService;

    @Operation(
            description = "User registration endpoint"
    )
    @PostMapping("/new")
    public ResponseObject<UserResponse> registerUser(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest) {
      log.info("Received user registration request: {}", userRegistrationRequest);

      return ResponseObject.<UserResponse>builder()
              .status(ResponseObject.ResponseStatus.SUCCESSFUL)
              .message("User registered successfully")
              .data(userService.registerUser(userRegistrationRequest))
              .build();
    }

    @GetMapping
    public ResponseObject<UsersPageResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        log.info("Received get all users request with page={}, size={}",  page, size);

        return ResponseObject.<UsersPageResponse>builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .data(userService.getAllUsers(page, size))
                .build();
    }
}
