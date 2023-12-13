package com.sstohnij.stacktraceqabackendv0.mapper;

import com.sstohnij.stacktraceqabackendv0.dto.response.RoleResponse;
import com.sstohnij.stacktraceqabackendv0.entity.Role;

public class RoleMapper {

    public static RoleResponse toRoleResponse(Role role) {
        return RoleResponse.builder().roleName(role.getRoleName()).build();
    }
}
