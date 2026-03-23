package com.allblue.lookbook.presentation.controller;

import com.allblue.common.response.ApiResponse;
import com.allblue.lookbook.application.LookbookCommandService;
import com.allblue.lookbook.application.LookbookQueryService;
import com.allblue.lookbook.application.dto.query.LookbookSearchQuery;
import com.allblue.lookbook.presentation.response.LookbookResponse;
import com.allblue.lookbook.presentation.response.LookbookResultCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adm/lookbooks")
@RequiredArgsConstructor
public class AdminLookbookController implements AdminLookbookApi {

    private final LookbookQueryService lookbookQueryService;
    private final LookbookCommandService lookbookCommandService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<List<LookbookResponse>>> findAll() {
        LookbookSearchQuery query = new LookbookSearchQuery(null, null, null, null, Integer.MAX_VALUE);
        List<LookbookResponse> response = lookbookQueryService.findAll(query).stream()
                .map(LookbookResponse::from)
                .toList();
        return ResponseEntity
                .status(LookbookResultCode.LOOKBOOK_LIST_OK.status())
                .body(ApiResponse.of(LookbookResultCode.LOOKBOOK_LIST_OK, response));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        lookbookCommandService.delete(id);
        return ResponseEntity
                .status(LookbookResultCode.LOOKBOOK_DELETED.status())
                .body(ApiResponse.from(LookbookResultCode.LOOKBOOK_DELETED));
    }
}
