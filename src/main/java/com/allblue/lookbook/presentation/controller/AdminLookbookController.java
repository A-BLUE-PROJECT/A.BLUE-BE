package com.allblue.lookbook.presentation.controller;

import com.allblue.admin.application.AdminInspectionCommandService;
import com.allblue.admin.security.AdminUserDetails;
import com.allblue.common.response.ApiResponse;
import com.allblue.lookbook.application.LookbookCommandService;
import com.allblue.lookbook.application.LookbookQueryService;
import com.allblue.lookbook.application.dto.query.LookbookSearchQuery;
import com.allblue.lookbook.presentation.response.LookbookResponse;
import com.allblue.lookbook.presentation.response.LookbookResultCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adm/v1/lookbooks")
@RequiredArgsConstructor
public class AdminLookbookController implements AdminLookbookApi {

    private final LookbookQueryService lookbookQueryService;
    private final LookbookCommandService lookbookCommandService;
    private final AdminInspectionCommandService adminInspectionCommandService;

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
    @PatchMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<Void>> approve(
            @PathVariable Long id,
            @AuthenticationPrincipal AdminUserDetails adminUserDetails) {
        adminInspectionCommandService.approveByLookbookId(id, adminUserDetails.adminId());
        return ResponseEntity.ok(ApiResponse.from(LookbookResultCode.LOOKBOOK_APPROVED));
    }

    @Override
    @PatchMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<Void>> reject(
            @PathVariable Long id,
            @AuthenticationPrincipal AdminUserDetails adminUserDetails) {
        adminInspectionCommandService.rejectByLookbookId(id, adminUserDetails.adminId());
        return ResponseEntity.ok(ApiResponse.from(LookbookResultCode.LOOKBOOK_REJECTED));
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
