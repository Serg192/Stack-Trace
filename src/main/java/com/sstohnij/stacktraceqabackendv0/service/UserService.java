package com.sstohnij.stacktraceqabackendv0.service;

import com.sstohnij.stacktraceqabackendv0.dto.request.UserRegistrationRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.UserResponse;

public interface UserService {
    UserResponse registerUser(UserRegistrationRequest userRegistrationRequest);
}
