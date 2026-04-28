package com.allblue.common.swagger;

import com.allblue.common.error.ErrorCode;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Swagger 문서화 시 필요한 에러 코드를 처리하기 위한 어노테이션입니다
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrorExceptions {
    Class<? extends ErrorCode>[] value();
}
