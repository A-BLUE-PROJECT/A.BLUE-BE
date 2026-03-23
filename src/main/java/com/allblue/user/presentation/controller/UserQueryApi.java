package com.allblue.user.presentation.controller;

import com.allblue.common.response.ApiResponse;
import com.allblue.security.oauth2.CustomUserDetails;
import com.allblue.user.presentation.response.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "?ъ???蹂?議고 API", description = "?ъ???蹂?議고 API")
public interface UserQueryApi {

    @Operation(summary = "???蹂?議고", description = "???濡洹?몃 ?ъ?? ????濡???蹂대?議고?⑸??")
    ResponseEntity<ApiResponse<UserInfoResponse>> getMyInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails);
}
