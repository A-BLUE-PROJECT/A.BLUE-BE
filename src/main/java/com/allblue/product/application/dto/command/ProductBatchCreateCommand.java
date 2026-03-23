package com.allblue.product.application.dto.command;

import com.allblue.product.domain.model.enums.MappedCategory;
import com.allblue.product.domain.model.enums.StockStatus;
import java.util.List;

public record ProductBatchCreateCommand(List<ProductCreateCommand> products) {

    public record ProductCreateCommand(
            Long sellerId,
            String externalProductId,
            String rawCategory,
            MappedCategory mappedCategory,
            String brandName,
            String productName,
            Integer price,
            Integer salePrice,
            String productImageUrl,
            String originUrl,
            StockStatus stockStatus) {}
}
