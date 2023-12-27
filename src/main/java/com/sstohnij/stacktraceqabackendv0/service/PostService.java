package com.sstohnij.stacktraceqabackendv0.service;

import com.sstohnij.stacktraceqabackendv0.dto.request.UpdatePostRequest;
import com.sstohnij.stacktraceqabackendv0.dto.request.LikeRequest;
import com.sstohnij.stacktraceqabackendv0.dto.request.PostsPageRequest;
import com.sstohnij.stacktraceqabackendv0.dto.request.UpdateCommentRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.CommentResponse;
import com.sstohnij.stacktraceqabackendv0.dto.response.LikeOpResponse;
import com.sstohnij.stacktraceqabackendv0.dto.response.PostResponse;
import com.sstohnij.stacktraceqabackendv0.dto.response.PostsPageResponse;


public interface PostService {

    PostResponse getPostById(Long id);
    PostResponse editPost(Long postId, UpdatePostRequest postRequest);
    PostResponse createPost(UpdatePostRequest postRequest);
    CommentResponse addCommentToPost(Long postId, UpdateCommentRequest request);
    LikeOpResponse likePost(Long postId, LikeRequest request);
    PostsPageResponse getAllPosts(PostsPageRequest request);
    void deletePost(Long postId);

}
