package com.sstohnij.stacktraceqabackendv0.service;

import com.sstohnij.stacktraceqabackendv0.dto.request.CreatePostRequest;
import com.sstohnij.stacktraceqabackendv0.dto.request.LikeRequest;
import com.sstohnij.stacktraceqabackendv0.dto.request.UpdateCommentRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.CommentResponse;
import com.sstohnij.stacktraceqabackendv0.dto.response.LikeOpResponse;
import com.sstohnij.stacktraceqabackendv0.dto.response.PostResponse;
import com.sstohnij.stacktraceqabackendv0.entity.Post;

import java.util.Optional;


public interface PostService {

    PostResponse getPostById(Long id);
    PostResponse createPost(CreatePostRequest postRequest);

    CommentResponse addCommentToPost(Long postId, UpdateCommentRequest request);

    LikeOpResponse likePost(Long postId, LikeRequest request);

}
