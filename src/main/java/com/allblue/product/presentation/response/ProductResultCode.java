package com.allblue.product.presentation.response;

import com.allblue.common.response.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ProductResultCode implements ResultCode {
    PRODUCT_BATCH_CREATED("SPD20101", "상품 배치 등록이 완료되었습니다.", HttpStatus.CREATED);

    private final String code;
    private final String message;
    private final HttpStatus status;

    @Override public HttpStatus status() { return status; }
    @Override public String code() { return code; }
    @Override public String message() { return message; }
}
