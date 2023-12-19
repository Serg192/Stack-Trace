package com.sstohnij.stacktraceqabackendv0.service;

import com.sstohnij.stacktraceqabackendv0.dto.request.CreatePostRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.PostResponse;
import com.sstohnij.stacktraceqabackendv0.entity.Post;

import java.util.Optional;

public interface PostService {

    PostResponse getPostById(Long id);
    PostResponse createPost(CreatePostRequest postRequest);

}
