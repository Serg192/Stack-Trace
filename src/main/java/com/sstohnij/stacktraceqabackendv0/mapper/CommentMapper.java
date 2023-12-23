package com.sstohnij.stacktraceqabackendv0.mapper;

import com.sstohnij.stacktraceqabackendv0.dto.response.CommentResponse;
import com.sstohnij.stacktraceqabackendv0.entity.Comment;

public class CommentMapper {

    public static CommentResponse toCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .user(UserMapper.toUserResponse(comment.getUser()))
                .publishDate(comment.getPublishDate())
                .content(comment.getContent())
                .build();
    }
}
