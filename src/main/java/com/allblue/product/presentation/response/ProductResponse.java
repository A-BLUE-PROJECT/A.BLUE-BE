package com.allblue.product.presentation.response;

import com.allblue.product.application.dto.result.ProductResult;
import com.allblue.product.domain.model.enums.MappedCategory;
import com.allblue.product.domain.model.enums.StockStatus;

public record ProductResponse(
        Long id,
        String brandName,
        String productName,
        Integer price,
        Integer salePrice,
        String productImageUrl,
        String originUrl,
        MappedCategory mappedCategory,
        StockStatus stockStatus,
        boolean hidden) {

    public static ProductResponse from(ProductResult result) {
        return new ProductResponse(
                result.id(),
                result.brandName(),
                result.productName(),
                result.price(),
                result.salePrice(),
                result.productImageUrl(),
                result.originUrl(),
                result.mappedCategory(),
                result.stockStatus(),
                result.hidden());
    }
}
