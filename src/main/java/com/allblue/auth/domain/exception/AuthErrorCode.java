package com.allblue.auth.domain.exception;

import com.allblue.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "EA40101", "???? ?? JWT ??????"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "EA40102", "留猷??JWT ??????"),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "EA40103", "吏??吏 ?? JWT ??????"),
    EMPTY_CLAIMS(HttpStatus.UNAUTHORIZED, "EA40104", "JWT ?대??臾몄?댁?鍮???????"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "EA40105", "???? ?? 由ы?? ??????"),
    INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "EA40107", "?紐????? JWT ?????????"),

    ABNORMAL_TOKEN_ACCESS(HttpStatus.FORBIDDEN, "EA40301", "鍮?????????洹??媛????듬?? 紐⑤ 湲곌린?? 濡洹???⑸??"),

    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "EA40901", "?대? ?ㅻⅨ ?? 怨??쇰? 媛?? ?대??쇱???");

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
