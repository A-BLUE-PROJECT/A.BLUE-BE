package com.allblue.lookbook.presentation.response;

import com.allblue.common.response.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum LookbookResultCode implements ResultCode {
    LOOKBOOK_GENERATE_ACCEPTED("SLB20201", "룩북 생성 요청이 접수되었습니다.", HttpStatus.ACCEPTED),
    MODEL_LIST_OK("SLB20008", "모델 이미지 목록 조회가 완료되었습니다.", HttpStatus.OK),
    LOOKBOOK_LIST_OK("SLB20001", "룩북 목록 조회가 완료되었습니다.", HttpStatus.OK),
    LOOKBOOK_DETAIL_OK("SLB20002", "룩북 상세 조회가 완료되었습니다.", HttpStatus.OK),
    LOOKBOOK_COMPLETED("SLB20003", "룩북 생성이 완료되었습니다.", HttpStatus.OK),
    LOOKBOOK_FAILED("SLB20004", "룩북 생성 실패 처리가 완료되었습니다.", HttpStatus.OK),
    LOOKBOOK_APPROVED("SLB20006", "룩북이 승인되었습니다.", HttpStatus.OK),
    LOOKBOOK_REJECTED("SLB20007", "룩북이 거부되었습니다.", HttpStatus.OK),
    LOOKBOOK_DELETED("SLB20005", "룩북이 삭제되었습니다.", HttpStatus.OK);

    private final String code;
    private final String message;
    private final HttpStatus status;

    @Override public HttpStatus status() { return status; }
    @Override public String code() { return code; }
    @Override public String message() { return message; }
}
