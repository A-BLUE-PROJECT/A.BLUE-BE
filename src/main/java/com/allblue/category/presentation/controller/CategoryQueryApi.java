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

@Tag(name = "愿由ъ 移댄怨由?議고 API", description = "愿由ъ媛 移댄怨由щ?議고?? API")
public interface CategoryQueryApi {

    @Operation(summary = "?泥?移댄怨由??몃━ 議고", description = "紐⑤ ?? 移댄怨由? ?? 移댄怨由щ??몃━ 援ъ“濡?議고?⑸??")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "移댄怨由??몃━ 議고 ?깃났",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = @ExampleObject(value = """
                                    {
                                      "code": "SCT20010",
                                      "message": "移댄怨由??몃━ 議고 ?깃났",
                                      "data": [
                                        {
                                          "categoryId": 1,
                                          "name": "怨?",
                                          "children": [
                                            {
                                              "categoryId": 2,
                                              "name": "遊?
                                            },
                                            {
                                              "categoryId": 3,
                                              "name": "?щ?"
                                            }
                                          ]
                                        }
                                      ]
                                    }""")))
            })
    ResponseEntity<ApiResponse<List<CategoryListResponse>>> getCategoryTree();
}
