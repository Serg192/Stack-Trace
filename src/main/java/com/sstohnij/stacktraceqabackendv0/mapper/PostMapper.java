package com.sstohnij.stacktraceqabackendv0.mapper;

import com.sstohnij.stacktraceqabackendv0.dto.request.CreatePostRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.PostResponse;
import com.sstohnij.stacktraceqabackendv0.dto.response.UserResponse;
import com.sstohnij.stacktraceqabackendv0.entity.Post;

public class PostMapper {

    public static Post fromPostRequest(CreatePostRequest createPostRequest){

        return Post.builder()
                .title(createPostRequest.getTitle())
                .postContent(createPostRequest.getPostContent())
               // .categories(createPostRequest.getCategories())
                .build();
    }

    public static PostResponse toPostResponse(Post post) {
       return PostResponse.builder()
               .id(post.getId())
               .user(UserMapper.toUserResponse(post.getUser()))
               .title(post.getTitle())
               .postContent(post.getPostContent())
               .publishDate(post.getPublishDate())
               .categories(post.getCategories())
               .postBanned(post.isPostBanned())
               .problemSolved(post.isProblemSolved())
               .build();
    }
}
