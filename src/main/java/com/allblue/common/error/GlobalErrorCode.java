package com.allblue.common.error;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "EG001", "?紐???泥????"),
    INVALID_JSON(HttpStatus.BAD_REQUEST, "EG002", "?泥 蹂몃Ц???쎌 ??????? JSON ???????댁＜?몄."),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "EG003", "?? ?泥 ??쇰명곌? ?????듬??"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EG004", "?湲곗?紐삵 ?ㅻ?媛 諛??????"),
    LOCK_ACQUISITION_FAILED(HttpStatus.CONFLICT, "EG005", "???泥由?以???泥??留???? ?? ???ㅼ ????二쇱??");
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public HttpStatus status() {
        return status;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
