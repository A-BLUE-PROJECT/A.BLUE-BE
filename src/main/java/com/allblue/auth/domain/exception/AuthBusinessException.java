package com.allblue.auth.domain.exception;

import com.allblue.common.error.BusinessException;
import com.allblue.common.error.ErrorCode;

public class AuthBusinessException extends BusinessException {
    public AuthBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
