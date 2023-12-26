package com.sstohnij.stacktraceqabackendv0.mapper;

import com.sstohnij.stacktraceqabackendv0.dto.internal.PostSummaryDTO;
import com.sstohnij.stacktraceqabackendv0.dto.request.UpdatePostRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.PostResponse;
import com.sstohnij.stacktraceqabackendv0.entity.Post;
import com.sstohnij.stacktraceqabackendv0.enums.LikeOpResult;

public class PostMapper {

    public static Post fromPostRequest(UpdatePostRequest createPostRequest){

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
               .userReaction(LikeOpResult.NONE)
               .likes(0L)
               .dislikes(0L)
               .comments(0L)
               .build();
    }

    public static PostResponse postSummaryToPostResponse(PostSummaryDTO summary) {
        PostResponse postResponse = PostMapper.toPostResponse(summary.getPost());
        postResponse.setLikes(summary.getLikesCount());
        postResponse.setDislikes(summary.getDislikesCount());
        postResponse.setComments(summary.getCommentsCount());
        return postResponse;
    }
}
