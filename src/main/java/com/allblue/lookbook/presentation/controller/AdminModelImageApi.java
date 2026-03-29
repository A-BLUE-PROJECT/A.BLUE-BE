package com.allblue.lookbook.presentation.controller;

import com.allblue.common.response.ApiResponse;
import com.allblue.lookbook.application.dto.result.ModelImageResult;
import com.allblue.lookbook.domain.model.enums.TargetGender;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Admin Model Image API", description = "모델 이미지 관리 API")
public interface AdminModelImageApi {

    @Operation(summary = "모델 이미지 목록 조회", description = "성별에 따른 모델 이미지 URL 목록을 반환합니다.")
    ResponseEntity<ApiResponse<List<ModelImageResult>>> getModelImages(
            @RequestParam(required = false) TargetGender gender);
}
