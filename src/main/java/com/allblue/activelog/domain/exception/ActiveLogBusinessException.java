package com.allblue.activelog.domain.exception;

import com.allblue.common.error.BusinessException;
import com.allblue.common.error.ErrorCode;

public class ActiveLogBusinessException extends BusinessException {
    public ActiveLogBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
