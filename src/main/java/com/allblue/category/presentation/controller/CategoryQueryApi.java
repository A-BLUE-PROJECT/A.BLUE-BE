package com.allblue.category.presentation.controller;

import com.allblue.category.presentation.response.CategoryListResponse;
import com.allblue.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "카테고리 조회 API", description = "카테고리 트리 구조를 조회하는 API")
public interface CategoryQueryApi {

    @Operation(summary = "전체 카테고리 트리 조회", description = "모든 카테고리 및 하위 카테고리를 트리 구조로 조회합니다")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "카테고리 트리 조회가 완료되었습니다",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = @ExampleObject(value = """
                                    {
                                      "code": "SCT20010",
                                      "message": "카테고리 트리 조회가 완료되었습니다",
                                      "data": [
                                        {
                                          "categoryId": 1,
                                          "name": "상의",
                                          "children": [
                                            {
                                              "categoryId": 2,
                                              "name": "하의"
                                            },
                                            {
                                              "categoryId": 3,
                                              "name": "원피스"
                                            }
                                          ]
                                        }
                                      ]
                                    }""")))
            })
    ResponseEntity<ApiResponse<List<CategoryListResponse>>> getCategoryTree();
}
