package com.allblue.auth.presentation.controller;

import com.allblue.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;

@Tag(name = "Auth API", description = "인증 관련 토큰 갱신 및 로그아웃 API (HttpOnly 쿠키 기반)")
public interface AuthApi {

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰으로 새로운 액세스 토큰 및 리프레시 토큰을 갱신합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "성공 (SA20001)"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "유효하지 않은 토큰 (EA40101)")
            })
    ResponseEntity<ApiResponse<Void>> refreshToken(
            @Parameter(hidden = true) @CookieValue(value = "refresh_token") String refreshToken,
            @Parameter(hidden = true) HttpServletResponse response);

    @Operation(summary = "로그아웃", description = "인증 관련 쿠키를 삭제(만료) 처리합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "로그아웃 성공 (SA20002)")
            })
    ResponseEntity<ApiResponse<Void>> logout(@Parameter(hidden = true) HttpServletResponse response);
}