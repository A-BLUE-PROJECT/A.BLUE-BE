package com.allblue.seller.presentation.response;

import com.allblue.common.response.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum SellerResultCode implements ResultCode {
    SELLER_AUTHORIZED("SSE20001", "카페24 OAuth 인증이 완료되었습니다.", HttpStatus.OK),
    SELLER_TOKEN_REFRESHED("SSE20002", "카페24 토큰이 갱신되었습니다.", HttpStatus.OK);

    private final String code;
    private final String message;
    private final HttpStatus status;

    @Override public HttpStatus status() { return status; }
    @Override public String code() { return code; }
    @Override public String message() { return message; }
}
