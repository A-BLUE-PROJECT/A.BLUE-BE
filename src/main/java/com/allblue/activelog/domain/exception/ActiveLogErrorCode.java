package com.allblue.activelog.domain.exception;

import com.allblue.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ActiveLogErrorCode implements ErrorCode {
    USER_ID_IS_REQUIRED_TO_CREATE(HttpStatus.BAD_REQUEST, "EAL40001", "?�용??ID???�수 값입?�다"),
    LOOKBOOK_ID_IS_REQUIRED_TO_CREATE(HttpStatus.BAD_REQUEST, "EAL40002", "룩북 ID는 필수 값입니다"),
    SWIPE_TYPE_IS_REQUIRED_TO_CREATE(HttpStatus.BAD_REQUEST, "EAL40003", "?��??�프 ?�?��? ?�수 값입?�다"),

    ACTIVE_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "EAL40401", "?��??�프 ?�력??찾을 ???�습?�다"),

    ALREADY_SWIPED(HttpStatus.CONFLICT, "EAL40901", "?��? ?��??�프 ?��?�??�료??카드?�니??"),
    ;

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
