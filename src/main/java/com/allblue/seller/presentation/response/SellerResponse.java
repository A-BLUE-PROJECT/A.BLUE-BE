package com.allblue.seller.presentation.response;

import com.allblue.seller.application.dto.result.SellerResult;
import java.time.LocalDateTime;

public record SellerResponse(
        Long id,
        String mallId,
        LocalDateTime tokenExpiresAt
) {
    public static SellerResponse from(SellerResult result) {
        return new SellerResponse(result.id(), result.mallId(), result.tokenExpiresAt());
    }
}
