package com.sstohnij.stacktraceqabackendv0.service;

import com.sstohnij.stacktraceqabackendv0.dto.request.UserRegistrationRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.UserResponse;
import com.sstohnij.stacktraceqabackendv0.dto.response.UsersPageResponse;
import com.sstohnij.stacktraceqabackendv0.entity.AppUser;
import org.springframework.data.domain.Page;

public interface UserService {
    UserResponse registerUser(UserRegistrationRequest userRegistrationRequest);
    UsersPageResponse getAllUsers(int page, int pageSize);
}
