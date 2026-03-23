package com.allblue.seller.domain.exception;

import com.allblue.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum SellerErrorCode implements ErrorCode {
    SELLER_NOT_FOUND("ESE40401", "셀러를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    TOKEN_EXCHANGE_FAILED("ESE50201", "카페24 토큰 발급에 실패했습니다.", HttpStatus.BAD_GATEWAY),
    TOKEN_REFRESH_FAILED("ESE50202", "카페24 토큰 갱신에 실패했습니다.", HttpStatus.BAD_GATEWAY);

    private final String code;
    private final String message;
    private final HttpStatus status;

    @Override public HttpStatus status() { return status; }
    @Override public String code() { return code; }
    @Override public String message() { return message; }
}
