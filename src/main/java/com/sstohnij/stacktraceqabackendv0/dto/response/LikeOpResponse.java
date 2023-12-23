package com.sstohnij.stacktraceqabackendv0.dto.response;

import com.sstohnij.stacktraceqabackendv0.enums.LikeOpResult;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeOpResponse {
    private LikeOpResult result;
}
