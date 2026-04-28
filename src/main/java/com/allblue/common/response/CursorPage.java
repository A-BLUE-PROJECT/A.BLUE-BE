package com.allblue.common.response;

import java.util.List;
import java.util.function.Function;

public record CursorPage<T>(List<T> items, boolean hasNext) {

    public static <T> CursorPage<T> of(List<T> rawItems, int requestedSize) {
        boolean hasNext = rawItems.size() > requestedSize;
        List<T> items = hasNext ? rawItems.subList(0, requestedSize) : rawItems;
        return new CursorPage<>(items, hasNext);
    }

    public <R> CursorPage<R> map(Function<T, R> mapper) {
        return new CursorPage<>(items.stream().map(mapper).toList(), hasNext);
    }
}
