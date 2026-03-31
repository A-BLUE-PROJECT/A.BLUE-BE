package com.allblue.lookbook.presentation.controller;

import com.allblue.common.response.ApiResponse;
import com.allblue.lookbook.application.dto.result.ModelImageResult;
import com.allblue.lookbook.domain.model.enums.TargetGender;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Admin Model Image API", description = "룩북 생성 시 사용할 모델 이미지 조회 API")
public interface AdminModelImageApi {

    @Operation(summary = "모델 이미지 목록 조회",
            description = "성별에 따른 모델 이미지 URL 목록을 반환합니다. 룩북 생성 화면에서 모델 선택 시 사용합니다. gender 미입력 시 WOMEN 기본값.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공 (SLB20008)")
    })
    ResponseEntity<ApiResponse<List<ModelImageResult>>> getModelImages(
            @Parameter(description = "성별 필터 (WOMEN, MEN). 미입력 시 WOMEN")
            @RequestParam(required = false) TargetGender gender);
}
