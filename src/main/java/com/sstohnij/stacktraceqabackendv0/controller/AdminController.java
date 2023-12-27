package com.sstohnij.stacktraceqabackendv0.controller;

import com.sstohnij.stacktraceqabackendv0.common.ResponseObject;
import com.sstohnij.stacktraceqabackendv0.dto.request.UpdateCategoryRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.BanResponse;
import com.sstohnij.stacktraceqabackendv0.entity.Category;
import com.sstohnij.stacktraceqabackendv0.service.AdminService;
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
@RequestMapping("/api/v0/admin")
@PreAuthorize("hasAuthority('type_admin')")
@SecurityRequirement(name = "Bearer Token")
@Tag(name = "Admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;
    @PostMapping("/banUser/{user_id}")
    @Operation(
            summary = "Ban/allow user to use website"
    )
    public ResponseObject<BanResponse> banUser(@PathVariable("user_id") Long userId) {
        log.info("Received ban user request with user id = {}", userId);

        return ResponseObject.<BanResponse>builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .data(adminService.banUserAccount(userId))
                .build();
    }

    @DeleteMapping("/comment/{comment_id}")
    @Operation(
            summary = "Delete user comment"
    )
    public ResponseEntity<?> deleteUserComment(@PathVariable("comment_id") Long commentId){
        log.info("Received delete user comment request with comment id = {}", commentId);

        adminService.deleteUserComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/post/{post_id}")
    @Operation(
            summary = "Delete user post"
    )
    public ResponseEntity<?> deleteUserPost(@PathVariable("post_id") Long postId){
        log.info("Received delete user post request with post id = {}", postId);

        adminService.deleteUserPost(postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/category")
    @Operation(
            summary = "Add new category"
    )
    public ResponseObject<Category> createNewCategory(@Valid @RequestBody UpdateCategoryRequest categoryRequest){
        log.info("Received add new category request with category title = {} and description = {}",
                categoryRequest.getTitle(),
                categoryRequest.getDescription()
        );

        return ResponseObject.<Category>builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .data(adminService.addNewCategory(categoryRequest))
                .build();
    }

    @PostMapping("/category/{category_id}")
    @Operation(
            summary = "Edit category"
    )
    public ResponseObject<?> editCategory(
        @PathVariable("category_id") Long categoryId,
        @Valid @RequestBody UpdateCategoryRequest categoryRequest
    ){
        log.info("Received update category request with category id = {}, title = {} and description = {}",
                categoryId,
                categoryRequest.getTitle(),
                categoryRequest.getDescription()
        );

        return ResponseObject.<Category>builder()
                .status(ResponseObject.ResponseStatus.SUCCESSFUL)
                .data(adminService.editCategory(categoryId, categoryRequest))
                .build();
    }

}
