package com.allblue.activelog.presentation.controller;

import com.allblue.activelog.application.ActiveLogCommandService;
import com.allblue.activelog.application.dto.command.SwipeCommand;
import com.allblue.activelog.presentation.request.SwipeRequest;
import com.allblue.activelog.presentation.response.ActiveLogResultCode;
import com.allblue.common.response.ApiResponse;
import com.allblue.security.oauth2.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ActiveLogController implements ActiveLogApi {

    private final ActiveLogCommandService activeLogCommandService;

    @Override
    @PostMapping("/w/v1/lookbooks/{lookbookId}/swipe")
    public ResponseEntity<ApiResponse<Void>> swipeCard(
            @PathVariable("lookbookId") Long lookbookId,
            @Valid @RequestBody SwipeRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        SwipeCommand command = new SwipeCommand(userDetails.getId(), lookbookId, request.swipeType());
        activeLogCommandService.saveSwipeAction(command);

        return ResponseEntity.ok(ApiResponse.from(ActiveLogResultCode.SWIPE_SUCCESS));
    }
}
