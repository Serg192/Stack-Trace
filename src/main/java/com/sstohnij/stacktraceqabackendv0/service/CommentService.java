package com.sstohnij.stacktraceqabackendv0.service;

import com.sstohnij.stacktraceqabackendv0.dto.request.UpdateCommentRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.CommentResponse;

public interface CommentService {

    CommentResponse editComment(Long commentId, UpdateCommentRequest request);

    CommentResponse getCommentById(Long commentId);
    void deleteComment(Long commentId);
}
