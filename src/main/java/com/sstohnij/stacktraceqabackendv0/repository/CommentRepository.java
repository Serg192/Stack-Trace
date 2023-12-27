package com.sstohnij.stacktraceqabackendv0.repository;

import com.sstohnij.stacktraceqabackendv0.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findCommentById(Long id);
    long countByPostId(Long postId);
}