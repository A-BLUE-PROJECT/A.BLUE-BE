package com.allblue.user.presentation.response;

import com.allblue.common.response.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserResultCode implements ResultCode {
    GET_MY_INFO_SUCCESS(HttpStatus.OK, "SU20002", "내 정보 조회가 완료되었습니다."),
    PROFILE_UPDATE_SUCCESS(HttpStatus.OK, "SU20003", "프로필 정보가 수정되었습니다."),
    USER_DELETE_SUCCESS(HttpStatus.OK, "SU20004", "회원 탈퇴가 처리되었습니다.");

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
