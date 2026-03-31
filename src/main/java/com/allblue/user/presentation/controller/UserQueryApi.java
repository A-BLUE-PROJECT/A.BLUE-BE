package com.allblue.user.presentation.controller;

import com.allblue.common.response.ApiResponse;
import com.allblue.security.oauth2.CustomUserDetails;
import com.allblue.user.presentation.response.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "사용자 정보 조회 API", description = "사용자 정보 조회 API")
public interface UserQueryApi {

    @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 개인정보를 조회합니다")
    ResponseEntity<ApiResponse<UserInfoResponse>> getMyInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails);
}
