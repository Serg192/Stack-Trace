package com.sstohnij.stacktraceqabackendv0.mapper;

import com.sstohnij.stacktraceqabackendv0.dto.request.UpdateCategoryRequest;
import com.sstohnij.stacktraceqabackendv0.entity.Category;

public class CategoryMapper {

    public static Category fromUpdateCategoryRequest(UpdateCategoryRequest request) {
        return Category.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .build();
    }
}
