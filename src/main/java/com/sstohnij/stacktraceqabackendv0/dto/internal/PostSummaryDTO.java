package com.sstohnij.stacktraceqabackendv0.dto.internal;

import com.sstohnij.stacktraceqabackendv0.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostSummaryDTO {
    private Post post;
    private Long likesCount;
    private Long dislikesCount;
    private Long commentsCount;
}
