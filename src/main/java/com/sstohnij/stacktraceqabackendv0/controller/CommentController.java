package com.sstohnij.stacktraceqabackendv0.controller;

import com.sstohnij.stacktraceqabackendv0.common.ResponseObject;
import com.sstohnij.stacktraceqabackendv0.dto.request.UpdateCommentRequest;
import com.sstohnij.stacktraceqabackendv0.dto.request.UpdatePostRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.CommentResponse;
import com.sstohnij.stacktraceqabackendv0.dto.response.PostResponse;
import com.sstohnij.stacktraceqabackendv0.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v0/comments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{comment_id}")
    @Operation(
            summary = "Get specific comment"
    )
    public ResponseObject<CommentResponse> getComment(@PathVariable("comment_id") Long commentId) {
        log.info("Received get comment request with comment_id={}", commentId);

        return ResponseObject.<CommentResponse>builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .data(commentService.getCommentById(commentId))
                .build();
    }

    @PostMapping("/{comment_id}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "Bearer Token")
    @Operation(
            summary = "Edit comment endpoint"
    )
    public ResponseObject<CommentResponse> editComment(
            @PathVariable("comment_id") Long commentId,
            @Valid @RequestBody UpdateCommentRequest request){

        log.info("Received edit comment request with comment id: {}, comment content: {}",
                commentId,
                request.getContent()
        );

        return ResponseObject.<CommentResponse>builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .data(commentService.editComment(commentId, request))
                .build();
    }

    @DeleteMapping("/{comment_id}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "Bearer Token")
    @Operation(
            summary = "Delete comment endpoint"
    )
    public ResponseEntity<?> deleteComment(@PathVariable("comment_id") Long commentId) {
        log.info("Received delete comment request with comment id: {}", commentId);
        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
