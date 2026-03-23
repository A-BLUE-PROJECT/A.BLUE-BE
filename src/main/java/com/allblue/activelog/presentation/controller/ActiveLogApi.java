package com.allblue.activelog.presentation.controller;

import com.allblue.activelog.domain.exception.ActiveLogErrorCode;
import com.allblue.activelog.presentation.request.SwipeRequest;
import com.allblue.common.response.ApiResponse;
import com.allblue.common.swagger.ApiErrorExceptions;
import com.allblue.security.oauth2.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "ActiveLog API", description = "룩북 스와이프(좋아요/싫어요) 기록 및 좋아요 기본 보관함 저장 API")
public interface ActiveLogApi {

    @Operation(summary = "룩북 스와이프 액션 저장", description = "특정 룩북에 대한 사용자의 액션을 기록합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공 (SAL20001)")
    @ApiErrorExceptions({ActiveLogErrorCode.class})
    ResponseEntity<ApiResponse<Void>> swipeCard(
            @Parameter(description = "룩북 ID", in = ParameterIn.PATH) Long lookbookId,
            @RequestBody(description = "스와이프 요청 정보 (LIKE/DISLIKE)") SwipeRequest request,
            @Parameter(hidden = true) CustomUserDetails userDetails);
}
