package com.sstohnij.stacktraceqabackendv0.repository;

import com.sstohnij.stacktraceqabackendv0.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    long countByPostId(Long postId);
}