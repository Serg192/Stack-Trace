package com.sstohnij.stacktraceqabackendv0.dto.response;

import com.sstohnij.stacktraceqabackendv0.entity.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private String email;

    private int rating;

    private boolean emailVerified;

    private boolean accountDeleted;

    private boolean accountBanned;

    private Set<RoleResponse> roles;
}
