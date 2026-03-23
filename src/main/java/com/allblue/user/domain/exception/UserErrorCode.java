package com.allblue.user.domain.exception;

import com.allblue.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    ALREADY_ONBOARDED(HttpStatus.BAD_REQUEST, "EU40001", "?��? ?�보?�이 ?�료???�용?�입?�다."),
    INVALID_USER_ID(HttpStatus.BAD_REQUEST, "EU40002", "?�효?��? ?��? ?�용??ID?�니??"),
    INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "EU40003", "?�네?��? 2???�상 10???�내?�야 ?�며, ?�수문자??'_'�??�용?�니??"),
    INVALID_PROFILE_INFO(HttpStatus.BAD_REQUEST, "EU40004", "?��? 몸무게는 ?�수 ?�력값입?�다."),
    INVALID_PROFILE_HEIGHT(HttpStatus.BAD_REQUEST, "EU40006", "?�는 100cm ?�상 220cm ?�하?�어???�니??"),
    INVALID_PROFILE_WEIGHT(HttpStatus.BAD_REQUEST, "EU40006", "몸무게는 30kg ?�상 150kg ?�하?�야 ?�니??"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "EU40401", "사용자를 찾을 수 없습니다."),
    FIT_PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "EU40402", "체형 정보를 찾을 수 없습니다."),
    FIT_PROFILE_ALREADY_EXISTS(HttpStatus.CONFLICT, "EU40901", "이미 등록된 체형 정보가 있습니다."),

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
