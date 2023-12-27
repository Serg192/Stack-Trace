package com.sstohnij.stacktraceqabackendv0.service.impl;

import com.sstohnij.stacktraceqabackendv0.dto.internal.PostSummaryDTO;
import com.sstohnij.stacktraceqabackendv0.dto.request.UpdateCategoryRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.BanResponse;
import com.sstohnij.stacktraceqabackendv0.entity.AppUser;
import com.sstohnij.stacktraceqabackendv0.entity.Category;
import com.sstohnij.stacktraceqabackendv0.entity.Comment;
import com.sstohnij.stacktraceqabackendv0.entity.Post;
import com.sstohnij.stacktraceqabackendv0.enums.RoleName;
import com.sstohnij.stacktraceqabackendv0.exception.custom.PermissionException;
import com.sstohnij.stacktraceqabackendv0.mapper.CategoryMapper;
import com.sstohnij.stacktraceqabackendv0.repository.AppUserRepository;
import com.sstohnij.stacktraceqabackendv0.repository.CategoryRepository;
import com.sstohnij.stacktraceqabackendv0.repository.CommentRepository;
import com.sstohnij.stacktraceqabackendv0.repository.PostRepository;
import com.sstohnij.stacktraceqabackendv0.service.AdminService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AppUserRepository appUserRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public BanResponse banUserAccount(Long userId) {
        AppUser user = appUserRepository.findAppUserById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id='%d' was not found in the database", userId))
        );

        if(isUserAdmin(user)) {
            throw new PermissionException("Admin can't ban user with role ADMIN");
        }

        user.setAccountBanned(!user.isAccountBanned());
        user = appUserRepository.save(user);

        return new BanResponse(user.isAccountBanned());
    }

    @Override
    public void deleteUserComment(Long commentId) {
        Comment comment = commentRepository.findCommentById(commentId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Comment with id='%d' was not found in the database", commentId))
        );

        if(isUserAdmin(comment.getUser())){
            throw new PermissionException("Admin can't delete comment of user with role ADMIN");
        }

        commentRepository.delete(comment);
    }

    @Override
    public void deleteUserPost(Long postId) {
        PostSummaryDTO summaryDTO = postRepository.getPostById(postId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Post with id='%d' was not found in the database", postId))
        );

        Post post = summaryDTO.getPost();
        if(isUserAdmin(post.getUser())){
            throw new PermissionException("Admin can't delete post of user with role ADMIN");
        }

        postRepository.delete(post);
    }

    @Override
    public Category addNewCategory(UpdateCategoryRequest categoryRequest) {
        Category newCategory = CategoryMapper.fromUpdateCategoryRequest(categoryRequest);
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category editCategory(Long categoryId, UpdateCategoryRequest categoryRequest) {
        Category category = categoryRepository.findCategoryById(categoryId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Category with id='%d' was not found in the database", categoryId))
        );

        category.setTitle(categoryRequest.getTitle());
        category.setDescription(categoryRequest.getDescription());
        return categoryRepository.save(category);
    }


    private boolean isUserAdmin(AppUser appUser) {
        return appUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleName.ROLE_ADMIN.str()));
    }

}
