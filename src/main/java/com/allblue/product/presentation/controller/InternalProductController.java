package com.allblue.product.presentation.controller;

import com.allblue.common.response.ApiResponse;
import com.allblue.product.application.ProductCommandService;
import com.allblue.product.presentation.request.ProductBatchCreateRequest;
import com.allblue.product.presentation.response.ProductResultCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/i/v1/products")
@RequiredArgsConstructor
public class InternalProductController implements InternalProductApi {

    private final ProductCommandService productCommandService;

    @Override
    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<Void>> batchCreate(
            @Valid @RequestBody ProductBatchCreateRequest request) {
        productCommandService.batchCreate(request.toCommand());
        return ResponseEntity
                .status(ProductResultCode.PRODUCT_BATCH_CREATED.status())
                .body(ApiResponse.from(ProductResultCode.PRODUCT_BATCH_CREATED));
    }
}
