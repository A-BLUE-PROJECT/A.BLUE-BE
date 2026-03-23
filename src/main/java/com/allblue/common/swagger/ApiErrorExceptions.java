package com.allblue.common.swagger;

import com.allblue.common.error.ErrorCode;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Swagger 臾몄???щ?鍮利???????щ?瑜????쇰? 異??湲??? ?대???????
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrorExceptions {
    Class<? extends ErrorCode>[] value();
}
