package com.sstohnij.stacktraceqabackendv0.dto.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.Set;
@Data
@Builder
public class PostsPageRequest {

    private int pageNumber;
    private int pageSize;

    private String sort;

    //Filters
    private String startDate;
    private String endDate;
    private Set<Long> categories;

}
