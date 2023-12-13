package com.sstohnij.stacktraceqabackendv0.mapper;

import com.sstohnij.stacktraceqabackendv0.dto.request.UserRegistrationRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.RoleResponse;
import com.sstohnij.stacktraceqabackendv0.dto.response.UserResponse;
import com.sstohnij.stacktraceqabackendv0.entity.AppUser;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static AppUser fromRegistrationRequest(UserRegistrationRequest userRegistrationRequest) {
        return AppUser
                .builder()
                .username(userRegistrationRequest.getUsername())
                .email(userRegistrationRequest.getEmail())
                .password(userRegistrationRequest.getPassword())
                .rating(0)
                .accountBanned(false)
                .accountDeleted(false)
                .emailVerified(false)
                .build();
    }
    public static UserResponse toUserResponse(AppUser user) {
        Set<RoleResponse> roles = user.getRoles().stream().map(RoleMapper::toRoleResponse).collect(Collectors.toSet());
        return UserResponse
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .rating(user.getRating())
                .accountBanned(user.isAccountBanned())
                .accountDeleted(user.isAccountDeleted())
                .emailVerified(user.isEmailVerified())
                .roles(roles)
                .build();
    }

}
