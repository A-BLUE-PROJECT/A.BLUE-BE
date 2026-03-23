package com.allblue.product.domain.exception;

import com.allblue.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ProductErrorCode implements ErrorCode {
    PRODUCT_NOT_FOUND("EPD40401", "상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PRODUCT_NAME_REQUIRED("EPD40001", "상품명은 필수입니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_EXTERNAL_ID_REQUIRED("EPD40002", "외부 상품 ID는 필수입니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;

    @Override public HttpStatus status() { return status; }
    @Override public String code() { return code; }
    @Override public String message() { return message; }
}
