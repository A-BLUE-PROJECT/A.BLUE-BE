package com.allblue.lookbook.presentation.controller;

import com.allblue.admin.security.AdminUserDetails;
import com.allblue.common.response.ApiResponse;
import com.allblue.lookbook.domain.model.enums.LookbookStatus;
import com.allblue.lookbook.presentation.request.LookbookGenerateRequest;
import com.allblue.lookbook.presentation.response.LookbookResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Admin Lookbook API", description = "룩북 어드민 API")
public interface AdminLookbookApi {

    @Operation(summary = "AI 룩북 생성 요청", description = "룩북 생성을 AI 워커에 비동기로 요청합니다. lookbookId를 즉시 반환합니다.")
    ResponseEntity<ApiResponse<Long>> generate(@RequestBody LookbookGenerateRequest request);

    @Operation(summary = "룩북 목록 조회", description = "상태별 룩북 목록을 조회합니다. status 미입력 시 전체 조회.")
    ResponseEntity<ApiResponse<List<LookbookResponse>>> findAll(
            @RequestParam(required = false) LookbookStatus status);

    @Operation(summary = "룩북 승인", description = "룩북을 승인하여 갤러리에 노출합니다.")
    ResponseEntity<ApiResponse<Void>> approve(
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal AdminUserDetails adminUserDetails);

    @Operation(summary = "룩북 거부", description = "룩북을 거부하여 갤러리 노출을 막습니다.")
    ResponseEntity<ApiResponse<Void>> reject(
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal AdminUserDetails adminUserDetails);

    @Operation(summary = "룩북 강제 삭제", description = "룩북을 Soft Delete 처리합니다.")
    ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id);
}
