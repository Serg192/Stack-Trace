package com.sstohnij.stacktraceqabackendv0.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostsPageResponse {
    private int total;
    private int pageSize;
    private int currentPage;
    private List<PostResponse> posts;
}
