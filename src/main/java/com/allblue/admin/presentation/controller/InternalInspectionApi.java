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

@Tag(name = "?대? ?????듭 肄諛?API", description = "?대? ????n8n) ?듭 寃??肄諛?? API")
public interface InternalInspectionApi {

    @Operation(summary = "AI 寃??寃곌낵 肄諛???")
    @ApiErrorExceptions(AdminErrorCode.class)
    ResponseEntity<ApiResponse<Void>> handleCallback(@Valid @RequestBody InspectionCallbackRequest request);
}
