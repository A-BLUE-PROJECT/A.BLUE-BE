package com.allblue.lookbook.presentation.controller;

import com.allblue.admin.security.AdminUserDetails;
import com.allblue.common.response.ApiResponse;
import com.allblue.common.swagger.ApiErrorExceptions;
import com.allblue.lookbook.domain.exception.LookbookErrorCode;
import com.allblue.lookbook.domain.model.enums.LookbookStatus;
import com.allblue.lookbook.presentation.request.LookbookGenerateRequest;
import com.allblue.lookbook.presentation.response.LookbookResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Admin Lookbook API", description = "룩북 어드민 관리 API")
public interface AdminLookbookApi {

    @Operation(summary = "AI 룩북 생성 요청",
            description = "룩북 생성을 AI 워커(n8n)에 비동기로 요청합니다. 생성된 lookbookId를 즉시 반환하며, 실제 이미지 합성은 백그라운드에서 처리됩니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "202", description = "생성 요청 접수 (SLB20201)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "아이템 없음 (ELB40002)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "상품 없음 (ELB40402)")
    })
    @ApiErrorExceptions(LookbookErrorCode.class)
    ResponseEntity<ApiResponse<Long>> generate(@RequestBody LookbookGenerateRequest request);

    @Operation(summary = "룩북 목록 조회", description = "상태별 룩북 목록을 조회합니다. status 미입력 시 전체 조회.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공 (SLB20001)")
    })
    ResponseEntity<ApiResponse<List<LookbookResponse>>> findAll(
            @Parameter(description = "룩북 상태 필터 (PENDING, COMPLETED, APPROVED, REJECTED, FAILED). 미입력 시 전체 조회")
            @RequestParam(required = false) LookbookStatus status);

    @Operation(summary = "룩북 승인", description = "룩북을 승인하여 B2C 갤러리에 노출합니다. 승인 시 Redis 캐시가 무효화됩니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "승인 성공 (SLB20006)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "COMPLETED 상태 아님 (ELB40003)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "룩북 없음 (ELB40401)")
    })
    @ApiErrorExceptions(LookbookErrorCode.class)
    ResponseEntity<ApiResponse<Void>> approve(
            @Parameter(description = "룩북 ID") @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal AdminUserDetails adminUserDetails);

    @Operation(summary = "룩북 거부", description = "룩북을 거부하여 갤러리 노출을 막습니다. 거부 시 Redis 캐시가 무효화됩니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "거부 성공 (SLB20007)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "COMPLETED 상태 아님 (ELB40003)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "룩북 없음 (ELB40401)")
    })
    @ApiErrorExceptions(LookbookErrorCode.class)
    ResponseEntity<ApiResponse<Void>> reject(
            @Parameter(description = "룩북 ID") @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal AdminUserDetails adminUserDetails);

    @Operation(summary = "룩북 삭제", description = "룩북을 Soft Delete 처리합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "삭제 성공 (SLB20005)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "룩북 없음 (ELB40401)")
    })
    @ApiErrorExceptions(LookbookErrorCode.class)
    ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "룩북 ID") @PathVariable Long id);
}
