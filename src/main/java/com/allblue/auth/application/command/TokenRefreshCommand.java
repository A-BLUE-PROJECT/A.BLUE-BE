package com.allblue.auth.application.command;

import com.allblue.auth.domain.exception.AuthBusinessException;
import com.allblue.auth.domain.exception.AuthErrorCode;
import org.springframework.util.StringUtils;

public record TokenRefreshCommand(String refreshToken) {
    public TokenRefreshCommand {
        if (!StringUtils.hasText(refreshToken)) {
            throw new AuthBusinessException(AuthErrorCode.INVALID_TOKEN);
        }
    }
}
