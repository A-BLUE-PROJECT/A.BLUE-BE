package com.allblue.seller.application.dto.result;

import com.allblue.seller.domain.model.Seller;
import java.time.LocalDateTime;

public record SellerResult(
        Long id,
        String mallId,
        LocalDateTime tokenExpiresAt
) {
    public static SellerResult from(Seller seller) {
        return new SellerResult(seller.getId(), seller.getMallId(), seller.getTokenExpiresAt());
    }
}
