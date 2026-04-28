package com.allblue.user.application.dto.result;

import com.allblue.user.domain.model.User;

public record UserInfoResult(
        Long id,
        String email,
        String status,
        String role) {

    public static UserInfoResult from(User user) {
        return new UserInfoResult(
                user.getId(),
                user.getEmail(),
                user.getStatus().name(),
                user.getRole().name());
    }
}
