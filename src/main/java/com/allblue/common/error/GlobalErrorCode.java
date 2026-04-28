package com.allblue.common.error;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "EG001", "유효성 검증 오류"),
    INVALID_JSON(HttpStatus.BAD_REQUEST, "EG002", "요청 본문을 파싱할 수 없는 JSON 형식입니다."),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "EG003", "필수 요청 파라미터가 누락됩니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EG004", "서버 내부 오류가 발생했습니다."),
    LOCK_ACQUISITION_FAILED(HttpStatus.CONFLICT, "EG005", "분산락 획득에 실패하여 요청을 처리할 수 없습니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public HttpStatus status() {
        return status;
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
