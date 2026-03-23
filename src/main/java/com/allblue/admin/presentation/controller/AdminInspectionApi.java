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

@Tag(name = "?대?吏 寃??愿由?API", description = "愿由ъ 諛깆?쇱???대?吏 寃??愿由?API")
public interface AdminInspectionApi {

    @Operation(summary = "??蹂??대?吏 寃??紐⑸? 議고 (湲곕낯媛? AI_PASSED, 蹂듭 ?? 媛??")
    @ApiErrorExceptions(AdminErrorCode.class)
    ResponseEntity<ApiResponse<PageResponse<ImageInspectionResponse>>> getInspections(
            @Parameter(description = "議고????媛?(蹂듭 媛?? 湲곕낯媛? AI_PASSED)") @RequestParam(defaultValue = "AI_PASSED")
                    List<InspectionStatus> status,
            Pageable pageable);

    @Operation(summary = "寃???? 蹂寃?(?뱀?諛??")
    @ApiErrorExceptions(AdminErrorCode.class)
    ResponseEntity<ApiResponse<Void>> updateInspectionStatus(
            @Parameter(description = "寃??ID") @PathVariable Long inspectionId,
            @Valid @RequestBody InspectionStatusUpdateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal AdminUserDetails adminUserDetails);
}
