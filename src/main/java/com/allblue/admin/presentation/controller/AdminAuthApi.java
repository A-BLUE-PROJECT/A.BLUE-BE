package com.allblue.admin.presentation.controller;

import com.allblue.admin.domain.exception.AdminErrorCode;
import com.allblue.admin.presentation.request.AdminLoginRequest;
import com.allblue.common.response.ApiResponse;
import com.allblue.common.swagger.ApiErrorExceptions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "Admin Auth API", description = "관리자 인증 API")
public interface AdminAuthApi {

    @Operation(summary = "관리자 로그인", description = "이메일과 비밀번호로 관리자 세션 토큰을 발급받습니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "관리자 로그인 성공")
    @ApiErrorExceptions(AdminErrorCode.class)
    ResponseEntity<ApiResponse<Void>> login(AdminLoginRequest request, HttpServletResponse response);

    @Operation(summary = "관리자 로그아웃", description = "관리자 로그아웃 처리를 수행합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "관리자 로그아웃 성공")
    @ApiErrorExceptions(AdminErrorCode.class)
    ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response);
}
