package com.allblue.admin.presentation.controller;

import com.allblue.admin.domain.exception.AdminErrorCode;
import com.allblue.admin.domain.model.InspectionStatus;
import com.allblue.admin.presentation.request.InspectionStatusUpdateRequest;
import com.allblue.admin.presentation.response.ImageInspectionResponse;
import com.allblue.admin.security.AdminUserDetails;
import com.allblue.common.response.ApiResponse;
import com.allblue.common.response.PageResponse;
import com.allblue.common.swagger.ApiErrorExceptions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "어드민 이미지 검수 API", description = "룩북 이미지에 대한 어드민 검수 API")
public interface AdminInspectionApi {

    @Operation(summary = "검수 목록 조회 (기본값: AI_PASSED, 복수 상태 조회 가능)")
    @ApiErrorExceptions(AdminErrorCode.class)
    ResponseEntity<ApiResponse<PageResponse<ImageInspectionResponse>>> getInspections(
            @Parameter(description = "조회할 상태 목록 (복수 가능, 기본값: AI_PASSED)") @RequestParam(defaultValue = "AI_PASSED")
                    List<InspectionStatus> status,
            Pageable pageable);

    @Operation(summary = "검수 상태 변경 (승인/거부)")
    @ApiErrorExceptions(AdminErrorCode.class)
    ResponseEntity<ApiResponse<Void>> updateInspectionStatus(
            @Parameter(description = "검수 ID") @PathVariable Long inspectionId,
            @Valid @RequestBody InspectionStatusUpdateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal AdminUserDetails adminUserDetails);
}
