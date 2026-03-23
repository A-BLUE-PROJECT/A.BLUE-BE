package com.allblue.user.presentation.controller;

import com.allblue.common.response.ApiResponse;
import com.allblue.security.oauth2.CustomUserDetails;
import com.allblue.user.application.FitProfileCommandService;
import com.allblue.user.application.FitProfileQueryService;
import com.allblue.user.presentation.request.FitProfileCreateRequest;
import com.allblue.user.presentation.request.FitProfileUpdateRequest;
import com.allblue.user.presentation.response.FitProfileResponse;
import com.allblue.user.presentation.response.FitProfileResultCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/w/v1/fit-profile")
@RequiredArgsConstructor
public class FitProfileController implements FitProfileApi {

    private final FitProfileCommandService fitProfileCommandService;
    private final FitProfileQueryService fitProfileQueryService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody FitProfileCreateRequest request) {
        fitProfileCommandService.create(request.toCommand(userDetails.getId()));
        return ResponseEntity
                .status(FitProfileResultCode.FIT_PROFILE_CREATED.status())
                .body(ApiResponse.from(FitProfileResultCode.FIT_PROFILE_CREATED));
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<FitProfileResponse>> findMyFitProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        FitProfileResponse response = FitProfileResponse.from(
                fitProfileQueryService.findByUserId(userDetails.getId()));
        return ResponseEntity
                .status(FitProfileResultCode.FIT_PROFILE_OK.status())
                .body(ApiResponse.of(FitProfileResultCode.FIT_PROFILE_OK, response));
    }

    @Override
    @PutMapping
    public ResponseEntity<ApiResponse<Void>> update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody FitProfileUpdateRequest request) {
        fitProfileCommandService.update(request.toCommand(userDetails.getId()));
        return ResponseEntity
                .status(FitProfileResultCode.FIT_PROFILE_UPDATED.status())
                .body(ApiResponse.from(FitProfileResultCode.FIT_PROFILE_UPDATED));
    }
}
