package com.allblue.lookbook.presentation.controller;

import com.allblue.common.response.ApiResponse;
import com.allblue.lookbook.application.LookbookQueryService;
import com.allblue.lookbook.application.dto.query.LookbookSearchQuery;
import com.allblue.lookbook.presentation.response.LookbookDetailResponse;
import com.allblue.lookbook.presentation.response.LookbookResponse;
import com.allblue.lookbook.presentation.response.LookbookResultCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/w/v1/lookbooks")
@RequiredArgsConstructor
public class LookbookController implements LookbookApi {

    private final LookbookQueryService lookbookQueryService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<List<LookbookResponse>>> findAll(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "20") int size) {
        LookbookSearchQuery query = new LookbookSearchQuery(cursor, size);
        List<LookbookResponse> response = lookbookQueryService.findAll(query).stream()
                .map(LookbookResponse::from)
                .toList();
        return ResponseEntity
                .status(LookbookResultCode.LOOKBOOK_LIST_OK.status())
                .body(ApiResponse.of(LookbookResultCode.LOOKBOOK_LIST_OK, response));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LookbookDetailResponse>> findById(@PathVariable Long id) {
        LookbookDetailResponse response = LookbookDetailResponse.from(lookbookQueryService.findById(id));
        return ResponseEntity
                .status(LookbookResultCode.LOOKBOOK_DETAIL_OK.status())
                .body(ApiResponse.of(LookbookResultCode.LOOKBOOK_DETAIL_OK, response));
    }
}
