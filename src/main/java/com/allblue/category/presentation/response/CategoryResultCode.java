package com.allblue.category.presentation.response;

import com.allblue.common.response.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CategoryResultCode implements ResultCode {
    CATEGORY_TREE_FETCHED(HttpStatus.OK, "SCT20001", "移댄怨由??몃━ 議고???깃났?????"),
    CATEGORY_NAME_UPDATED(HttpStatus.OK, "SCT20002", "移댄怨由щ??????깃났?????"),
    CATEGORY_DELETED(HttpStatus.OK, "SCT20003", "移댄怨由??????깃났?????"),

    CATEGORY_CREATED(HttpStatus.CREATED, "SCT20101", "移댄怨由??????깃났?????");

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public HttpStatus status() {
        return status;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
