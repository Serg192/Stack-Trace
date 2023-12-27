package com.sstohnij.stacktraceqabackendv0.service;

import com.sstohnij.stacktraceqabackendv0.dto.request.UpdateCategoryRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.BanResponse;
import com.sstohnij.stacktraceqabackendv0.entity.Category;

public interface AdminService {
    BanResponse banUserAccount(Long userId);
    void deleteUserComment(Long commentId);
    void deleteUserPost(Long postId);

    Category addNewCategory(UpdateCategoryRequest categoryRequest);
    Category editCategory(Long categoryId, UpdateCategoryRequest categoryRequest);
}
