package com.allblue.lookbook.presentation.controller;

import com.allblue.common.response.ApiResponse;
import com.allblue.lookbook.presentation.request.LookbookCompleteRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Internal Lookbook API", description = "룩북 내부 API (n8n AI 콜백용)")
public interface InternalLookbookApi {

    @Operation(summary = "룩북 생성 완료 콜백", description = "AI 이미지 합성 완료 시 n8n이 호출하는 콜백 엔드포인트입니다.")
    ResponseEntity<ApiResponse<Void>> complete(
            @PathVariable Long id,
            @Valid @RequestBody LookbookCompleteRequest request);

    @Operation(summary = "룩북 생성 실패 콜백", description = "AI 이미지 합성 실패 시 n8n이 호출하는 콜백 엔드포인트입니다.")
    ResponseEntity<ApiResponse<Void>> fail(@PathVariable Long id);
}
