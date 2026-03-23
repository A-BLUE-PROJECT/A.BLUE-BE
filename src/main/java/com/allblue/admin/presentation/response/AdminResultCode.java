package com.allblue.admin.presentation.response;

import com.allblue.common.response.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AdminResultCode implements ResultCode {
    ADMIN_LOGIN_SUCCESS(HttpStatus.OK, "SAD20001", "愿由ъ 濡洹?몄 ?깃났?????"),
    ADMIN_LOGOUT_SUCCESS(HttpStatus.OK, "SAD20002", "愿由ъ 濡洹?????깃났?????"),
    INSPECTION_STATUS_UPDATED(HttpStatus.OK, "SAD20003", "?대?吏 寃????媛 ?깃났??쇰?蹂寃쎈?????"),
    INSPECTION_LIST_QUERIED(HttpStatus.OK, "SAD20004", "?대?吏 寃??紐⑸? 議고???깃났?????"),
    INSPECTION_CALLBACK_SUCCESS(HttpStatus.OK, "SAD20005", "AI 寃??寃곌낵媛 ?깃났??쇰?諛????듬??");

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
