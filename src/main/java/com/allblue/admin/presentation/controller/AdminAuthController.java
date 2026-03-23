package com.allblue.admin.presentation.controller;

import com.allblue.admin.application.AdminAuthService;
import com.allblue.admin.application.dto.result.AdminLoginResult;
import com.allblue.admin.presentation.request.AdminLoginRequest;
import com.allblue.admin.presentation.response.AdminResultCode;
import com.allblue.auth.presentation.util.CookieUtil;
import com.allblue.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adm/v1/auth")
@RequiredArgsConstructor
public class AdminAuthController implements AdminAuthApi {

    private static final String ADMIN_TOKEN_COOKIE_NAME = "admin_access_token";

    private final AdminAuthService adminAuthService;
    private final CookieUtil cookieUtil;

    @Value("${jwt.admin-access-token-validity-in-seconds}")
    private int cookieMaxAge;

    @Override
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(
            @Valid @RequestBody AdminLoginRequest request, HttpServletResponse response) {

        AdminLoginResult result = adminAuthService.login(request.toCommand());

        cookieUtil.addCookie(response, ADMIN_TOKEN_COOKIE_NAME, result.accessToken(), cookieMaxAge);

        return ResponseEntity.ok(ApiResponse.from(AdminResultCode.ADMIN_LOGIN_SUCCESS));
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response) {

        adminAuthService.logout();

        cookieUtil.deleteCookie(response, ADMIN_TOKEN_COOKIE_NAME);

        return ResponseEntity.ok(ApiResponse.from(AdminResultCode.ADMIN_LOGOUT_SUCCESS));
    }
}
