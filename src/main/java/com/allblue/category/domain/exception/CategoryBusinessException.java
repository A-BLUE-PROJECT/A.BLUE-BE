package com.allblue.category.domain.exception;

import com.allblue.common.error.BusinessException;
import com.allblue.common.error.ErrorCode;

public class CategoryBusinessException extends BusinessException {
    public CategoryBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
