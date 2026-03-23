package com.allblue.lookbook.domain.exception;

import com.allblue.common.error.BusinessException;

public class LookbookBusinessException extends BusinessException {
    public LookbookBusinessException(LookbookErrorCode errorCode) {
        super(errorCode);
    }
}
