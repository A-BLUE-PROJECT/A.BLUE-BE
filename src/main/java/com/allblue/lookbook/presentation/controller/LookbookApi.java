package com.allblue.lookbook.presentation.controller;

import com.allblue.common.response.ApiResponse;
import com.allblue.lookbook.presentation.response.LookbookDetailResponse;
import com.allblue.lookbook.presentation.response.LookbookResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Lookbook API", description = "룩북 갤러리 조회 API")
public interface LookbookApi {

    @Operation(summary = "룩북 목록 조회", description = "ALLBLUE AI가 생성한 룩북 갤러리 목록을 조회합니다.")
    ResponseEntity<ApiResponse<List<LookbookResponse>>> findAll();

    @Operation(summary = "룩북 상세 조회", description = "룩북 ID로 상세 정보를 조회합니다.")
    ResponseEntity<ApiResponse<LookbookDetailResponse>> findById(@PathVariable Long id);
}
