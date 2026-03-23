package com.allblue.category.domain.exception;

import com.allblue.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CategoryErrorCode implements ErrorCode {
    CATEGORY_NAME_IS_REQUIRED(HttpStatus.BAD_REQUEST, "ECT40001", "카테고리 이름은 필수입니다."),
    CATEGORY_NAME_TOO_LONG(HttpStatus.BAD_REQUEST, "ECT40002", "카테고리 이름은 10자 이하여야 합니다."),
    PARENT_CATEGORY_REQUIRED(HttpStatus.BAD_REQUEST, "ECT40003", "하위 카테고리는 상위 카테고리가 필요합니다."),
    ONLY_PARENT_CAN_HAVE_CHILDREN(HttpStatus.BAD_REQUEST, "ECT40004", "상위 카테고리만 하위 카테고리를 가질 수 있습니다."),
    ONLY_CHILD_CATEGORY_CAN_MAP_CARD(HttpStatus.BAD_REQUEST, "ECT40005", "하위 카테고리만 룩북에 매핑될 수 있습니다."),

    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "ECT40401", "카테고리를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public HttpStatus status() {
        return httpStatus;
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