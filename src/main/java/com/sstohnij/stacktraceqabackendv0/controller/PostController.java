package com.sstohnij.stacktraceqabackendv0.controller;

import com.sstohnij.stacktraceqabackendv0.common.ResponseObject;
import com.sstohnij.stacktraceqabackendv0.dto.request.CreatePostRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.PostResponse;
import com.sstohnij.stacktraceqabackendv0.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v0/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @GetMapping("/{post_id}")
    public ResponseObject<PostResponse> getPost(@PathVariable("post_id") Long postId) {
        log.info("Received get post request with post_id={}", postId);

        return ResponseObject.<PostResponse>builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .data(postService.getPostById(postId))
                .build();
    }

    @PostMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseObject<PostResponse> createNewPost(@Valid @RequestBody CreatePostRequest createPostRequest){
        log.info("Received create post request with post data -> title: '{}', content: '{}', categories: {}",
                createPostRequest.getTitle(),
                createPostRequest.getPostContent(),
                createPostRequest.getCategories());

        return ResponseObject.<PostResponse>builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .data(postService.createPost(createPostRequest))
                .build();
    }
}
