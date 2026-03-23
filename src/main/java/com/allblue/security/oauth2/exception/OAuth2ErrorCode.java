package com.allblue.security.oauth2.exception;

import com.allblue.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum OAuth2ErrorCode implements ErrorCode {
    MISSING_USER_INFO(HttpStatus.BAD_REQUEST, "EA40001", "?? 濡洹???怨?濡遺???? ?ъ???蹂대?諛? 紐삵?듬??"),
    UNSUPPORTED_PROVIDER(HttpStatus.BAD_REQUEST, "EA40002", "吏??吏 ?? ?? 濡洹???怨?????"),

    OAUTH2_AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "EA40003", "?? 濡洹???몄????ㅽ?????");

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
