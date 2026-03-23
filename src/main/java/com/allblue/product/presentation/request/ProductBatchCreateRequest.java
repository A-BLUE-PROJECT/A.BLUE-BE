package com.allblue.product.presentation.request;

import com.allblue.product.application.dto.command.ProductBatchCreateCommand;
import com.allblue.product.domain.model.enums.MappedCategory;
import com.allblue.product.domain.model.enums.StockStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ProductBatchCreateRequest(
        @NotEmpty @Valid List<ProductCreateRequest> products) {

    public record ProductCreateRequest(
            Long sellerId,
            String externalProductId,
            String rawCategory,
            MappedCategory mappedCategory,
            @NotBlank String brandName,
            @NotBlank String productName,
            Integer price,
            Integer salePrice,
            String productImageUrl,
            String originUrl,
            @NotNull StockStatus stockStatus) {}

    public ProductBatchCreateCommand toCommand() {
        return new ProductBatchCreateCommand(
                products.stream()
                        .map(p -> new ProductBatchCreateCommand.ProductCreateCommand(
                                p.sellerId(),
                                p.externalProductId(),
                                p.rawCategory(),
                                p.mappedCategory(),
                                p.brandName(),
                                p.productName(),
                                p.price(),
                                p.salePrice(),
                                p.productImageUrl(),
                                p.originUrl(),
                                p.stockStatus()))
                        .toList());
    }
}
