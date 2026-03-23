package com.allblue.user.presentation.response;

import com.allblue.common.response.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum FitProfileResultCode implements ResultCode {
    FIT_PROFILE_CREATED("SU20101", "체형 정보가 등록되었습니다.", HttpStatus.CREATED),
    FIT_PROFILE_OK("SU20102", "체형 정보 조회가 완료되었습니다.", HttpStatus.OK),
    FIT_PROFILE_UPDATED("SU20103", "체형 정보가 수정되었습니다.", HttpStatus.OK);

    private final String code;
    private final String message;
    private final HttpStatus status;

    @Override public HttpStatus status() { return status; }
    @Override public String code() { return code; }
    @Override public String message() { return message; }
}
