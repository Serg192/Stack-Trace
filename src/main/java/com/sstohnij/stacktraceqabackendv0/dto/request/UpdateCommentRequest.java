package com.sstohnij.stacktraceqabackendv0.dto.request;

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
public class UpdateCommentRequest {

    @NotNull(message = "Comment content is required")
    @Size(min = 10, message = "Comment content should be at least 10 characters")
    @Size(max = 500, message = "Comment content can't be longer then 500 characters")
    private String content;
}
