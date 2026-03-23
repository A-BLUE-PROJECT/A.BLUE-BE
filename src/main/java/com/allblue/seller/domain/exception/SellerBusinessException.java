package com.allblue.seller.domain.exception;

import com.allblue.common.error.BusinessException;

public class SellerBusinessException extends BusinessException {
    public SellerBusinessException(SellerErrorCode errorCode) {
        super(errorCode);
    }
}
