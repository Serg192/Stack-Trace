package com.sstohnij.stacktraceqabackendv0.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class RoleResponse {
    private String roleName;
}
