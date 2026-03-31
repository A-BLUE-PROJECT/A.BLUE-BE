package com.allblue.category.presentation.controller;

import com.allblue.category.domain.exception.CategoryErrorCode;
import com.allblue.category.presentation.request.CreateCategoryRequest;
import com.allblue.category.presentation.request.UpdateCategoryNameRequest;
import com.allblue.category.presentation.response.CreateCategoryResponse;
import com.allblue.common.response.ApiResponse;
import com.allblue.common.swagger.ApiErrorExceptions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

@Tag(name = "Admin Category API", description = "관리자 카테고리 생성/수정/삭제 API")
public interface CategoryCommandApi {

    @Operation(summary = "상위 카테고리 생성", description = "최상위 카테고리를 생성합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "생성 성공 (SCT20101)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효하지 않은 이름 (ECT40001, ECT40002)")
    })
    @ApiErrorExceptions(CategoryErrorCode.class)
    ResponseEntity<ApiResponse<CreateCategoryResponse>> createParentCategory(@Valid CreateCategoryRequest request);

    @Operation(summary = "하위 카테고리 생성", description = "지정한 상위 카테고리 하위에 카테고리를 생성합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "생성 성공 (SCT20101)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 계층 구조 (ECT40003, ECT40004)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "상위 카테고리 없음 (ECT40401)")
    })
    @ApiErrorExceptions(CategoryErrorCode.class)
    ResponseEntity<ApiResponse<CreateCategoryResponse>> createChildCategory(
            @Parameter(description = "상위 카테고리 ID", in = ParameterIn.PATH) Long parentId,
            @Valid CreateCategoryRequest request);

    @Operation(summary = "카테고리 이름 수정", description = "상위 또는 하위 카테고리의 이름을 수정합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공 (SCT20002)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효하지 않은 이름 (ECT40001, ECT40002)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "카테고리 없음 (ECT40401)")
    })
    @ApiErrorExceptions(CategoryErrorCode.class)
    ResponseEntity<ApiResponse<Void>> updateCategoryName(
            @Parameter(description = "카테고리 ID", in = ParameterIn.PATH) Long categoryId,
            @Valid UpdateCategoryNameRequest request);

    @Operation(summary = "상위 카테고리 삭제", description = "상위 카테고리를 삭제합니다. 하위 카테고리 및 관련 매핑도 함께 삭제됩니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "삭제 성공 (SCT20003)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "카테고리 없음 (ECT40401)")
    })
    @ApiErrorExceptions(CategoryErrorCode.class)
    ResponseEntity<ApiResponse<Void>> deleteParentCategory(
            @Parameter(description = "상위 카테고리 ID", in = ParameterIn.PATH) Long categoryId);

    @Operation(summary = "하위 카테고리 삭제", description = "하위 카테고리를 삭제합니다. 관련 룩북 매핑도 함께 삭제됩니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "삭제 성공 (SCT20003)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "카테고리 없음 (ECT40401)")
    })
    @ApiErrorExceptions(CategoryErrorCode.class)
    ResponseEntity<ApiResponse<Void>> deleteChildCategory(
            @Parameter(description = "하위 카테고리 ID", in = ParameterIn.PATH) Long categoryId);
}
