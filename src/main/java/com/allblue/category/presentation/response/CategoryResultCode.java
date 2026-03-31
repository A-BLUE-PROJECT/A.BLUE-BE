package com.allblue.category.presentation.response;

import com.allblue.common.response.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CategoryResultCode implements ResultCode {
    CATEGORY_TREE_FETCHED(HttpStatus.OK, "SCT20001", "카테고리 트리 조회가 완료되었습니다."),
    CATEGORY_NAME_UPDATED(HttpStatus.OK, "SCT20002", "카테고리명 수정이 완료되었습니다."),
    CATEGORY_DELETED(HttpStatus.OK, "SCT20003", "카테고리 삭제가 완료되었습니다."),

    CATEGORY_CREATED(HttpStatus.CREATED, "SCT20101", "카테고리 생성이 완료되었습니다.");

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
