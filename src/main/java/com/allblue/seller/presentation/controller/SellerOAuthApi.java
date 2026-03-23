package com.allblue.seller.presentation.controller;

import com.allblue.common.response.ApiResponse;
import com.allblue.seller.presentation.response.SellerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "Seller OAuth API", description = "카페24 셀러 OAuth 인증 API")
public interface SellerOAuthApi {

    @Operation(summary = "카페24 OAuth 인증 시작", description = "카페24 OAuth 인증 페이지로 리다이렉트합니다.")
    void authorize(
            @Parameter(description = "카페24 쇼핑몰 ID", in = ParameterIn.QUERY) String mallId,
            HttpServletResponse response) throws Exception;

    @Operation(summary = "카페24 OAuth 콜백", description = "카페24 인증 코드를 Access Token으로 교환하고 셀러 정보를 저장합니다.")
    ResponseEntity<ApiResponse<SellerResponse>> callback(
            @Parameter(description = "인증 코드", in = ParameterIn.QUERY) String code,
            @Parameter(description = "카페24 쇼핑몰 ID", in = ParameterIn.QUERY) String mallId);

    @Operation(summary = "카페24 Access Token 갱신", description = "만료된 Access Token을 Refresh Token으로 갱신합니다.")
    ResponseEntity<ApiResponse<SellerResponse>> refreshToken(
            @Parameter(description = "카페24 쇼핑몰 ID", in = ParameterIn.PATH) String mallId);
}
