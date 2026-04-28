package com.allblue.product.presentation.controller;

import com.allblue.common.response.ApiResponse;
import com.allblue.common.swagger.ApiErrorExceptions;
import com.allblue.product.domain.exception.ProductErrorCode;
import com.allblue.product.domain.model.enums.MappedCategory;
import com.allblue.product.presentation.request.ProductHiddenRequest;
import com.allblue.product.presentation.response.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Admin Product API", description = "어드민 상품 관리 API")
public interface AdminProductApi {

    @Operation(summary = "상품 목록 조회", description = "카테고리별 상품 목록을 조회합니다. category 미입력 시 전체 조회.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공 (SPD20001)")
    })
    ResponseEntity<ApiResponse<List<ProductResponse>>> findAll(
            @Parameter(description = "카테고리 필터 (TOP, BOTTOM, OUTER, ACC). 미입력 시 전체 조회")
            @RequestParam(required = false) MappedCategory category);

    @Operation(summary = "상품 숨김/공개 처리", description = "상품의 갤러리 노출 여부를 변경합니다. hidden=true이면 갤러리에서 숨깁니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "변경 성공 (SPD20002)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "상품 없음 (EPD40401)")
    })
    @ApiErrorExceptions(ProductErrorCode.class)
    ResponseEntity<ApiResponse<Void>> updateHidden(
            @Parameter(description = "상품 ID") @PathVariable Long id,
            @RequestBody ProductHiddenRequest request);
}
