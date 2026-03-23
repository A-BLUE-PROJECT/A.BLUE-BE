package com.allblue.seller.presentation.controller;

import com.allblue.common.response.ApiResponse;
import com.allblue.seller.application.SellerOAuthService;
import com.allblue.seller.application.dto.result.SellerResult;
import com.allblue.seller.presentation.response.SellerResponse;
import com.allblue.seller.presentation.response.SellerResultCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/s/v1/oauth")
@RequiredArgsConstructor
public class SellerOAuthController implements SellerOAuthApi {

    private final SellerOAuthService sellerOAuthService;

    @GetMapping("/authorize")
    @Override
    public void authorize(@RequestParam("mall_id") String mallId, HttpServletResponse response) throws Exception {
        String authorizationUrl = sellerOAuthService.buildAuthorizationUrl(mallId);
        response.sendRedirect(authorizationUrl);
    }

    @GetMapping("/callback")
    @Override
    public ResponseEntity<ApiResponse<SellerResponse>> callback(
            @RequestParam("code") String code,
            @RequestParam("mall_id") String mallId) {
        SellerResult result = sellerOAuthService.handleCallback(mallId, code);
        return ResponseEntity.ok(ApiResponse.of(SellerResultCode.SELLER_AUTHORIZED, SellerResponse.from(result)));
    }

    @PostMapping("/{mallId}/refresh")
    @Override
    public ResponseEntity<ApiResponse<SellerResponse>> refreshToken(@PathVariable String mallId) {
        SellerResult result = sellerOAuthService.refreshToken(mallId);
        return ResponseEntity.ok(ApiResponse.of(SellerResultCode.SELLER_TOKEN_REFRESHED, SellerResponse.from(result)));
    }
}
