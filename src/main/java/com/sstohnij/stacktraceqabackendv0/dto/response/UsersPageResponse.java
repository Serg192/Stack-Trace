package com.sstohnij.stacktraceqabackendv0.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UsersPageResponse {
    private int totalPages;
    private int currentPage;
    private List<UserResponse> users;
}
