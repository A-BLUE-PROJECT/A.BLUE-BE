package com.allblue.user.presentation.response;

import com.allblue.common.response.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserResultCode implements ResultCode {
    ONBOARDING_SUCCESS(HttpStatus.OK, "SU20001", "?⑤낫?⑹??깃났??쇰??猷???듬??"),
    GET_MY_INFO_SUCCESS(HttpStatus.OK, "SU20002", "???蹂?議고???깃났?????"),

    PROFILE_UPDATE_SUCCESS(HttpStatus.OK, "SU20003", "?濡?????댄???깃났?????"),
    USER_DELETE_SUCCESS(HttpStatus.OK, "SU20004", "?? ??닿? ?猷???듬??");

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
