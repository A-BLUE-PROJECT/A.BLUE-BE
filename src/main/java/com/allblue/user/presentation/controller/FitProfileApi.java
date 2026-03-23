package com.allblue.user.presentation.controller;

import com.allblue.common.response.ApiResponse;
import com.allblue.security.oauth2.CustomUserDetails;
import com.allblue.user.presentation.request.FitProfileCreateRequest;
import com.allblue.user.presentation.request.FitProfileUpdateRequest;
import com.allblue.user.presentation.response.FitProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "FitProfile API", description = "체형 정보 API")
public interface FitProfileApi {

    @Operation(summary = "체형 정보 등록", description = "로그인한 사용자의 체형 정보를 등록합니다.")
    ResponseEntity<ApiResponse<Void>> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody FitProfileCreateRequest request);

    @Operation(summary = "체형 정보 조회", description = "로그인한 사용자의 체형 정보를 조회합니다.")
    ResponseEntity<ApiResponse<FitProfileResponse>> findMyFitProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails);

    @Operation(summary = "체형 정보 수정", description = "로그인한 사용자의 체형 정보를 수정합니다.")
    ResponseEntity<ApiResponse<Void>> update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody FitProfileUpdateRequest request);
}
