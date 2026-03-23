package com.allblue.lookbook.presentation.controller;

import com.allblue.common.response.ApiResponse;
import com.allblue.lookbook.application.LookbookCommandService;
import com.allblue.lookbook.presentation.request.LookbookCompleteRequest;
import com.allblue.lookbook.presentation.response.LookbookResultCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/i/v1/lookbooks")
@RequiredArgsConstructor
public class InternalLookbookController implements InternalLookbookApi {

    private final LookbookCommandService lookbookCommandService;

    @Override
    @PostMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<Void>> complete(
            @PathVariable Long id,
            @Valid @RequestBody LookbookCompleteRequest request) {
        lookbookCommandService.complete(request.toCommand(id));
        return ResponseEntity
                .status(LookbookResultCode.LOOKBOOK_COMPLETED.status())
                .body(ApiResponse.from(LookbookResultCode.LOOKBOOK_COMPLETED));
    }

    @Override
    @PostMapping("/{id}/fail")
    public ResponseEntity<ApiResponse<Void>> fail(@PathVariable Long id) {
        lookbookCommandService.fail(id);
        return ResponseEntity
                .status(LookbookResultCode.LOOKBOOK_FAILED.status())
                .body(ApiResponse.from(LookbookResultCode.LOOKBOOK_FAILED));
    }
}
