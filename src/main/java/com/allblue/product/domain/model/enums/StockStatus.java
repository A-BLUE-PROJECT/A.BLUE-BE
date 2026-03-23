package com.allblue.product.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StockStatus {
    IN_STOCK("재고 있음"),
    OUT_OF_STOCK("품절");

    private final String description;
}
