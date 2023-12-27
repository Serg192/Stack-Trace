package com.sstohnij.stacktraceqabackendv0.dto.request;

import com.sstohnij.stacktraceqabackendv0.validation.CategoryTitle;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryRequest {

    @NotNull(message = "Category title is required")
    @Size(min = 1, message = "Category title should be at least 1 characters")
    @Size(max = 20, message = "Category title can't be longer then 20 characters")
    @CategoryTitle
    private String title;

    @NotNull(message = "Category description is required")
    @Size(min = 10, message = "Category description should be at least 10 characters")
    @Size(max = 200, message = "Category description can't be longer then 200 characters")
    private String description;
}
