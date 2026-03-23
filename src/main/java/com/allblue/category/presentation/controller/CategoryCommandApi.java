package com.allblue.category.presentation.controller;

import com.allblue.category.presentation.request.CreateCategoryRequest;
import com.allblue.category.presentation.request.UpdateCategoryNameRequest;
import com.allblue.category.presentation.response.CreateCategoryResponse;
import com.allblue.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Admin Category API", description = "관리자 카테고리 생성/삭제 API")
public interface CategoryCommandApi {

    @Operation(summary = "상위 카테고리 생성", description = "최상위 카테고리를 생성합니다.")
    ResponseEntity<ApiResponse<CreateCategoryResponse>> createParentCategory(@Valid CreateCategoryRequest request);

    @Operation(summary = "하위 카테고리 생성", description = "상위 카테고리 하위에 카테고리를 생성합니다.")
    ResponseEntity<ApiResponse<CreateCategoryResponse>> createChildCategory(
            @Parameter(description = "상위 카테고리 ID", in = ParameterIn.PATH) Long parentId,
            @Valid CreateCategoryRequest request);

    @Operation(summary = "카테고리 이름 수정", description = "특정 상위 또는 하위 카테고리 이름을 수정합니다.")
    ResponseEntity<ApiResponse<Void>> updateCategoryName(
            @Parameter(description = "카테고리 ID", in = ParameterIn.PATH) Long categoryId,
            @Valid UpdateCategoryNameRequest request);

    @Operation(summary = "상위 카테고리 삭제", description = "상위 카테고리를 삭제합니다. 관련 룩북 매핑도 함께 삭제됩니다.")
    ResponseEntity<ApiResponse<Void>> deleteParentCategory(
            @Parameter(description = "상위 카테고리 ID", in = ParameterIn.PATH) Long categoryId);

    @Operation(summary = "하위 카테고리 삭제", description = "하위 카테고리를 삭제합니다. 관련 룩북 매핑도 함께 삭제됩니다.")
    ResponseEntity<ApiResponse<Void>> deleteChildCategory(
            @Parameter(description = "하위 카테고리 ID", in = ParameterIn.PATH) Long categoryId);
}