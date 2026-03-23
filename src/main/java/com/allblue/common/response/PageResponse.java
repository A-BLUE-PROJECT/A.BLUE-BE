package com.allblue.common.response;

import java.util.List;
import org.springframework.data.domain.Page;

public record PageResponse<T>(
        List<T> content, int currentPage, int size, long totalElements, int totalPages, boolean hasNext) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext());
    }

    public static <T> PageResponse<T> from(List<T> list, int pageSize) {
        return new PageResponse<>(list, 0, pageSize, list.size(), list.isEmpty() ? 0 : 1, false);
    }
}
