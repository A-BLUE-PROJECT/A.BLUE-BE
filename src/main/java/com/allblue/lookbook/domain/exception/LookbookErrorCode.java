package com.allblue.lookbook.domain.exception;

import com.allblue.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum LookbookErrorCode implements ErrorCode {
    PRODUCT_NOT_FOUND("ELB40402", "Product not found", HttpStatus.NOT_FOUND),
    LOOKBOOK_NOT_FOUND("ELB40401", "룩북을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    LOOKBOOK_STATUS_NOT_PENDING("ELB40001", "룩북 상태가 PENDING이 아닙니다.", HttpStatus.BAD_REQUEST),
    LOOKBOOK_ITEMS_REQUIRED("ELB40002", "룩북 아이템은 최소 1개 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    LOOKBOOK_STATUS_NOT_COMPLETED("ELB40003", "룩북 상태가 COMPLETED가 아닙니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;

    @Override public HttpStatus status() { return status; }
    @Override public String code() { return code; }
    @Override public String message() { return message; }
}
