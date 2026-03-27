package com.allblue.product.presentation.controller;

import com.allblue.common.response.ApiResponse;
import com.allblue.product.application.ProductCommandService;
import com.allblue.product.application.ProductQueryService;
import com.allblue.product.domain.model.enums.MappedCategory;
import com.allblue.product.presentation.request.ProductHiddenRequest;
import com.allblue.product.presentation.response.ProductResponse;
import com.allblue.product.presentation.response.ProductResultCode;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adm/v1/products")
@RequiredArgsConstructor
public class AdminProductController implements AdminProductApi {

    private final ProductQueryService productQueryService;
    private final ProductCommandService productCommandService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> findAll(
            @RequestParam(required = false) MappedCategory category) {
        List<ProductResponse> response = productQueryService.findAllForAdmin(category).stream()
                .map(ProductResponse::from)
                .toList();
        return ResponseEntity.ok(ApiResponse.of(ProductResultCode.PRODUCT_LIST_OK, response));
    }

    @Override
    @PatchMapping("/{id}/hidden")
    public ResponseEntity<ApiResponse<Void>> updateHidden(
            @PathVariable Long id,
            @Valid @RequestBody ProductHiddenRequest request) {
        productCommandService.updateHidden(id, request.hidden());
        return ResponseEntity.ok(ApiResponse.from(ProductResultCode.PRODUCT_HIDDEN_UPDATED));
    }
}
