package com.allblue.common.error;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈?�스 ?�외 처리
     * */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.errorCode();
        log.warn("Business Exception : [Code: {}] {}", errorCode.code(), errorCode.message());
        return ResponseEntity.status(e.errorCode().status()).body(ErrorResponse.from(errorCode));
    }

    /**
     * Request DTO Valid 검�??�류 처리
     * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " : " + fieldError.getDefaultMessage())
                .toList();

        log.info("Validation Exception : {}", errors);

        return buildErrorResponse(GlobalErrorCode.VALIDATION_ERROR, errors);
    }

    /**
     * PathVariable / RequestParam Validated 검�??�패 처리
     * */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + " : " + v.getMessage())
                .toList();

        log.info("Constraint Violation : {}", errors);

        return buildErrorResponse(GlobalErrorCode.VALIDATION_ERROR, errors);
    }

    /**
     * JSON ?�싱 ?�류 처리
     * */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseError(HttpMessageNotReadableException e) {
        log.info("JSON Parse Error : {}", e.getMessage());

        return buildErrorResponse(GlobalErrorCode.INVALID_JSON, List.of(e.getMessage()));
    }

    /**
     * ?�수 ?�청 ?�라미터 ?�락 처리
     * */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException e) {
        String error = e.getParameterName() + " ?�라미터가 ?�요?�니??";
        log.info("Missing Parameter : {}", error);

        return buildErrorResponse(GlobalErrorCode.MISSING_PARAMETER, List.of(error));
    }

    /**
     * ?�상�?못한 ?�버 ?�류 처리
     * */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e) {
        log.error("Unexpected Error Occurred : ", e);

        return buildErrorResponse(GlobalErrorCode.INTERNAL_ERROR, List.of(e.getMessage()));
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(ErrorCode errorCode, List<String> errors) {
        return ResponseEntity.status(errorCode.status()).body(ErrorResponse.of(errorCode, errors));
    }
}
