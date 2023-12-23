package com.sstohnij.stacktraceqabackendv0.repository;

import com.sstohnij.stacktraceqabackendv0.entity.AppUser;
import com.sstohnij.stacktraceqabackendv0.entity.Like;
import com.sstohnij.stacktraceqabackendv0.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(AppUser user, Post post);

    long countByPostIdAndIsDislikeIsTrue(Long postId);
    long countByPostIdAndIsDislikeIsFalse(Long postId);
}
