package com.allblue.lookbook.presentation.controller;

import com.allblue.common.response.ApiResponse;
import com.allblue.common.swagger.ApiErrorExceptions;
import com.allblue.lookbook.domain.exception.LookbookErrorCode;
import com.allblue.lookbook.presentation.request.LookbookCompleteRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Internal Lookbook API", description = "룩북 내부 콜백 API - n8n AI 워커 전용")
public interface InternalLookbookApi {

    @Operation(summary = "룩북 생성 완료 콜백",
            description = "AI 이미지 합성 완료 시 n8n이 호출하는 엔드포인트입니다. 생성된 이미지 URL과 aiScore를 저장하고 ImageInspection을 자동 생성합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "처리 성공 (SLB20003)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "PENDING 상태 아님 (ELB40001)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "룩북 없음 (ELB40401)")
    })
    @ApiErrorExceptions(LookbookErrorCode.class)
    ResponseEntity<ApiResponse<Void>> complete(
            @Parameter(description = "룩북 ID") @PathVariable Long id,
            @Valid @RequestBody LookbookCompleteRequest request);

    @Operation(summary = "룩북 생성 실패 콜백",
            description = "AI 이미지 합성 실패 시 n8n이 호출하는 엔드포인트입니다. 재시도 횟수를 증가시키고 3회 초과 시 FAILED로 처리합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "실패 처리 성공 (SLB20004)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "룩북 없음 (ELB40401)")
    })
    @ApiErrorExceptions(LookbookErrorCode.class)
    ResponseEntity<ApiResponse<Void>> fail(
            @Parameter(description = "룩북 ID") @PathVariable Long id);
}
