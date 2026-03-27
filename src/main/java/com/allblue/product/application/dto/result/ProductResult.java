package com.allblue.product.application.dto.result;

import com.allblue.product.domain.model.Product;
import com.allblue.product.domain.model.enums.MappedCategory;
import com.allblue.product.domain.model.enums.StockStatus;

public record ProductResult(
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

    public static ProductResult from(Product product) {
        return new ProductResult(
                product.getId(),
                product.getBrandName(),
                product.getProductName(),
                product.getPrice(),
                product.getSalePrice(),
                product.getProductImageUrl(),
                product.getOriginUrl(),
                product.getMappedCategory(),
                product.getStockStatus(),
                product.isHidden());
    }
}
