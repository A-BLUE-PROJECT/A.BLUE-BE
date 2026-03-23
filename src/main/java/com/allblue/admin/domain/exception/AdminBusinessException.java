package com.allblue.admin.domain.exception;

import com.allblue.common.error.BusinessException;
import com.allblue.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class AdminBusinessException extends BusinessException {

    private final ErrorCode errorCode;

    public AdminBusinessException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
