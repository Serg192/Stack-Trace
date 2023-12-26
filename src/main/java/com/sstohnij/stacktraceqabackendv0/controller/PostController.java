package com.sstohnij.stacktraceqabackendv0.controller;

import com.sstohnij.stacktraceqabackendv0.common.ResponseObject;
import com.sstohnij.stacktraceqabackendv0.dto.request.UpdatePostRequest;
import com.sstohnij.stacktraceqabackendv0.dto.request.LikeRequest;
import com.sstohnij.stacktraceqabackendv0.dto.request.PostsPageRequest;
import com.sstohnij.stacktraceqabackendv0.dto.request.UpdateCommentRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.CommentResponse;
import com.sstohnij.stacktraceqabackendv0.dto.response.LikeOpResponse;
import com.sstohnij.stacktraceqabackendv0.dto.response.PostResponse;
import com.sstohnij.stacktraceqabackendv0.dto.response.PostsPageResponse;
import com.sstohnij.stacktraceqabackendv0.service.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v0/posts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Posts")
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
    @SecurityRequirement(name = "Bearer Token")
    public ResponseObject<PostResponse> createNewPost(@Valid @RequestBody UpdatePostRequest createPostRequest){
        log.info("Received create post request with post data -> title: '{}', content: '{}', categories: {}",
                createPostRequest.getTitle(),
                createPostRequest.getPostContent(),
                createPostRequest.getCategories());

        return ResponseObject.<PostResponse>builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .data(postService.createPost(createPostRequest))
                .build();
    }

    @PostMapping("/{post_id}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "Bearer Token")
    public ResponseObject<PostResponse> editPost(
            @PathVariable("post_id") Long postId,
            @Valid @RequestBody UpdatePostRequest updatePostRequest
    ){
        log.info("Received edit post request with post data -> title: '{}', content: '{}', categories: {}",
                updatePostRequest.getTitle(),
                updatePostRequest.getPostContent(),
                updatePostRequest.getCategories());

        return ResponseObject.<PostResponse>builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .data(postService.editPost(postId, updatePostRequest))
                .build();
    }

    @PostMapping("/{post_id}/comment")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "Bearer Token")
    public ResponseObject<CommentResponse> createNewPostComment(
            @PathVariable("post_id") Long postId,
            @Valid @RequestBody UpdateCommentRequest commentRequest
    ){
        log.info("Received add post comment request with post_id={}, comment_content={}",
                postId,
                commentRequest.getContent());

        return ResponseObject.<CommentResponse>builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .data(postService.addCommentToPost(postId, commentRequest))
                .build();
    }

    @PostMapping("/{post_id}/like")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "Bearer Token")
    public ResponseObject<LikeOpResponse> likePost(
            @PathVariable("post_id" )Long postId,
            @Valid @RequestBody LikeRequest request
    ){
        log.info("Received like post request, like type is -> {}", !request.getIsDislike());

        return ResponseObject.<LikeOpResponse>builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .data(postService.likePost(postId, request))
                .build();
    }


    @GetMapping
    public ResponseObject<PostsPageResponse> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "MOST_LIKED") String sort,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Set<Long> categories
            ){

        PostsPageRequest postsPageRequest = PostsPageRequest.builder()
                .pageNumber(page)
                .pageSize(pageSize)
                .sort(sort)
                .startDate(startDate)
                .endDate(endDate)
                .categories(categories)
                .build();

        log.info("Get all post request received: {}", postsPageRequest);

        return ResponseObject.<PostsPageResponse>builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .data(postService.getAllPosts(postsPageRequest))
                .build();
    }
}
