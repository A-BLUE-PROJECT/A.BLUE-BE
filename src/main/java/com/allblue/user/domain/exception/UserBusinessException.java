package com.allblue.user.domain.exception;

import com.allblue.common.error.BusinessException;
import com.allblue.common.error.ErrorCode;

public class UserBusinessException extends BusinessException {
    public UserBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
