package com.allblue.admin.presentation.response;

import com.allblue.common.response.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AdminResultCode implements ResultCode {
    ADMIN_LOGIN_SUCCESS(HttpStatus.OK, "SAD20001", "어드민 로그인이 완료되었습니다."),
    ADMIN_LOGOUT_SUCCESS(HttpStatus.OK, "SAD20002", "어드민 로그아웃이 완료되었습니다."),
    INSPECTION_STATUS_UPDATED(HttpStatus.OK, "SAD20003", "검수 상태 변경이 완료되었습니다."),
    INSPECTION_LIST_QUERIED(HttpStatus.OK, "SAD20004", "검수 목록 조회가 완료되었습니다."),
    INSPECTION_CALLBACK_SUCCESS(HttpStatus.OK, "SAD20005", "AI 검수 결과 반영이 완료되었습니다.");

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
