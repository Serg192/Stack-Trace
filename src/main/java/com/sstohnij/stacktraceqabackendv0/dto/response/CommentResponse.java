package com.sstohnij.stacktraceqabackendv0.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private Long id;

    private UserResponse user;

    private Date publishDate;

    private String content;
}
