package com.allblue.auth.domain.exception;

import com.allblue.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "EA40101", "유효하지 않은 JWT 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "EA40102", "만료된 JWT 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "EA40103", "지원하지 않는 JWT 토큰입니다."),
    EMPTY_CLAIMS(HttpStatus.UNAUTHORIZED, "EA40104", "JWT 토큰의 클레임이 비어있습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "EA40105", "유효하지 않은 리프레시 토큰입니다."),
    INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "EA40107", "올바르지 않은 JWT 토큰 타입입니다."),

    ABNORMAL_TOKEN_ACCESS(HttpStatus.FORBIDDEN, "EA40301", "토큰이 탈취된 것으로 감지되어 보안상의 이유로 로그아웃 합니다."),

    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "EA40901", "이미 가입된 이메일 주소가 있는 계정입니다.");

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
