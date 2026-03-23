package com.allblue.auth.presentation.response;

import com.allblue.common.response.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthResultCode implements ResultCode {
    TOKEN_REFRESH_SUCCESS(HttpStatus.OK, "SA20001", "????щ?湲??깃났"),
    LOGOUT_SUCCESS(HttpStatus.OK, "SA20002", "濡洹?? ?깃났");

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
