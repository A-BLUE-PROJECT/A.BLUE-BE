package com.allblue.user.presentation.response;

import com.allblue.user.application.dto.result.UserInfoResult;

public record UserInfoResponse(
        Long id,
        String email,
        String status,
        String role) {

    public static UserInfoResponse from(UserInfoResult result) {
        return new UserInfoResponse(
                result.id(),
                result.email(),
                result.status(),
                result.role());
    }
}
