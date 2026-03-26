package com.allblue.user.domain.exception;

import com.allblue.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "EU40003", "닉네임은 2자 이상 10자 이내여야 하며, 특수문자는 '_'만 사용합니다."),
    INVALID_PROFILE_HEIGHT(HttpStatus.BAD_REQUEST, "EU40006", "키는 100cm 이상 220cm 이하여야 합니다."),
    INVALID_PROFILE_WEIGHT(HttpStatus.BAD_REQUEST, "EU40007", "몸무게는 30kg 이상 150kg 이하여야 합니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "EU40401", "사용자를 찾을 수 없습니다."),

    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "EU40902", "이미 사용 중인 닉네임입니다.");

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
