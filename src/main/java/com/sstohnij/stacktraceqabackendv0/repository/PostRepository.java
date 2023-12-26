package com.sstohnij.stacktraceqabackendv0.repository;

import com.sstohnij.stacktraceqabackendv0.dto.internal.PostSummaryDTO;
import com.sstohnij.stacktraceqabackendv0.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT NEW com.sstohnij.stacktraceqabackendv0.dto.internal.PostSummaryDTO ( " +
            "p," +
            "COUNT(CASE WHEN l.isDislike = false THEN 1 END)," +
            "COUNT(CASE WHEN l.isDislike = true THEN 1 END)," +
            "COUNT(DISTINCT c))" +
            "FROM Post p " +
            "LEFT JOIN Like l ON l.post = p " +
            "LEFT JOIN Comment c ON c.post = p " +
            "WHERE " +
            "p.id = :id " +
            "GROUP BY p")
    Optional<PostSummaryDTO> getPostById(@Param("id") Long id);

    @Query("SELECT NEW com.sstohnij.stacktraceqabackendv0.dto.internal.PostSummaryDTO( " +
            "p, " +
            "COUNT(CASE WHEN l.isDislike = false THEN 1 END), " +
            "COUNT(CASE WHEN l.isDislike = true THEN 1 END), " +
            "COUNT(DISTINCT c))" +
            "FROM Post p " +
            "LEFT JOIN Like l ON l.post = p " +
            "LEFT JOIN Comment c ON c.post = p " +
            "WHERE " +
            "(:categories IS NULL OR  " +
            "  (SELECT COUNT(pc.id) FROM p.categories pc WHERE pc.id IN :categories) = :cat_count) AND " +
            "(:startDate IS NULL OR p.publishDate >= :startDate) AND " +
            "(:endDate IS NULL OR p.publishDate <= :endDate) " +
            "GROUP BY p " +
            "ORDER BY " +
            "CASE WHEN :sort = 'MOST_LIKED' THEN COUNT(CASE WHEN l.isDislike = false THEN 1 END) END DESC, " +
            "CASE WHEN :sort = 'LEAST_LIKED' THEN COUNT(CASE WHEN l.isDislike = false THEN 1 END) END ASC, " +
            "CASE WHEN :sort = 'MOST_RECENT' THEN p.publishDate END DESC, " +
            "CASE WHEN :sort = 'MOST_OLD' THEN p.publishDate END ASC ")
    Page<PostSummaryDTO> getSorterAndFilteredPostPage(@Param("sort") String sort,
                                                        @Param("categories") Set<Long> categories,
                                                        @Param("cat_count") Long catCount,
                                                        @Param("startDate") Date startDate,
                                                        @Param("endDate") Date endDate,
                                                        Pageable pageable);

}
