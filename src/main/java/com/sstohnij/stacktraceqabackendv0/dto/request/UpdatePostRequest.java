package com.sstohnij.stacktraceqabackendv0.dto.request;

import com.sstohnij.stacktraceqabackendv0.validation.PostCategorySet;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostRequest {

    @NotNull(message = "Title is required")
    @Size(min = 10, message = "Post title should be at least 10 characters")
    @Size(max = 100, message = "Post title can't be longer then 100 characters")
    private String title;

    @NotNull(message = "Post content is required")
    @Size(min = 10, message = "Post content should be at least 10 characters")
    @Size(max = 1000, message = "Post content can't be longer then 1000 characters")
    private String postContent;

    @NotEmpty(message = "At least one category is required")
    @PostCategorySet
    private Set<Long> categories;
}
