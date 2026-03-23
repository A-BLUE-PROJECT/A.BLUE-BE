package com.allblue.product.domain.exception;

import com.allblue.common.error.BusinessException;

public class ProductBusinessException extends BusinessException {
    public ProductBusinessException(ProductErrorCode errorCode) {
        super(errorCode);
    }
}
