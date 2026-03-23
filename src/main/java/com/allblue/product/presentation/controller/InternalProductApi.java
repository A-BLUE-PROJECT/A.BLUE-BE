package com.allblue.product.presentation.controller;

import com.allblue.common.response.ApiResponse;
import com.allblue.product.presentation.request.ProductBatchCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Internal Product API", description = "상품 내부 API (크롤러/카페24 동기화용)")
public interface InternalProductApi {

    @Operation(summary = "상품 배치 등록", description = "크롤러 또는 카페24 동기화에서 상품 데이터를 배치로 등록합니다.")
    ResponseEntity<ApiResponse<Void>> batchCreate(@Valid @RequestBody ProductBatchCreateRequest request);
}
