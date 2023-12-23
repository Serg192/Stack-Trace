package com.sstohnij.stacktraceqabackendv0.dto.response;

import com.sstohnij.stacktraceqabackendv0.entity.AppUser;
import com.sstohnij.stacktraceqabackendv0.entity.Category;
import com.sstohnij.stacktraceqabackendv0.enums.LikeOpResult;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

        private Long id;

        private UserResponse user;

        private String title;

        private String postContent;

        private Date publishDate;

        private Long likes;

        private Long dislikes;

        private Long comments;

        private LikeOpResult userReaction;

        private Set<Category> categories;

        private boolean problemSolved;

        private boolean postBanned;
}
