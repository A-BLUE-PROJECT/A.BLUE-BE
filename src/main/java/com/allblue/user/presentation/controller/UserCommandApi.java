package com.allblue.user.presentation.controller;

import com.allblue.common.response.ApiResponse;
import com.allblue.security.oauth2.CustomUserDetails;
import com.allblue.user.presentation.request.UserOnboardingRequest;
import com.allblue.user.presentation.request.UserProfileUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User Command API", description = "사용자 정보 수정 API")
public interface UserCommandApi {

    @Operation(summary = "회원 온보딩", description = "최초 로그인 이후 프로필 정보를 입력하고 기본 보관함을 생성합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "성공 (SU20001)"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "필수값 누락(EG001) / 이미 온보딩된 사용자(EU40001) / 닉네임 오류(EU40003)",
                        content =
                                @Content(schema = @Schema(implementation = com.allblue.common.error.ErrorResponse.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "사용자 없음(EU40401)",
                        content =
                                @Content(schema = @Schema(implementation = com.allblue.common.error.ErrorResponse.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "이미 사용 중인 닉네임(EU40901)",
                        content =
                                @Content(schema = @Schema(implementation = com.allblue.common.error.ErrorResponse.class)))
            })
    ResponseEntity<ApiResponse<Void>> onboardUser(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserOnboardingRequest request);

    @Operation(summary = "프로필 수정", description = "로그인한 사용자의 프로필 정보를 수정합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "성공 (SU20003)"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "사용자 없음(EU40401)",
                        content =
                                @Content(schema = @Schema(implementation = com.allblue.common.error.ErrorResponse.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "이미 사용 중인 닉네임(EU40901)",
                        content =
                                @Content(schema = @Schema(implementation = com.allblue.common.error.ErrorResponse.class)))
            })
    ResponseEntity<ApiResponse<Void>> updateProfile(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserProfileUpdateRequest request);

    @Operation(summary = "회원 탈퇴", description = "사용자 계정을 비활성화하고 관련 데이터를 처리합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "성공 (SU20004)"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "사용자 없음(EU40401)",
                        content =
                                @Content(schema = @Schema(implementation = com.allblue.common.error.ErrorResponse.class)))
            })
    ResponseEntity<ApiResponse<Void>> deleteUser(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails);
}