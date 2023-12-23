package com.sstohnij.stacktraceqabackendv0.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeRequest {

    @NotNull(message = "Request body should have like type: {isDislike: 'bool'}")
    private Boolean isDislike;
}
