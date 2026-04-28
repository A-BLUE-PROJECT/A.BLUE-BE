package com.allblue.admin.presentation.controller;

import com.allblue.admin.domain.exception.AdminErrorCode;
import com.allblue.admin.presentation.request.InspectionCallbackRequest;
import com.allblue.common.response.ApiResponse;
import com.allblue.common.swagger.ApiErrorExceptions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "내부 검수 콜백 API", description = "검수 시스템(n8n)에서 수신하는 결과 콜백 API")
public interface InternalInspectionApi {

    @Operation(summary = "AI 검수 결과 콜백 수신")
    @ApiErrorExceptions(AdminErrorCode.class)
    ResponseEntity<ApiResponse<Void>> handleCallback(@Valid @RequestBody InspectionCallbackRequest request);
}
