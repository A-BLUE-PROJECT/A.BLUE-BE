package com.allblue.lookbook.application.port.out;

import java.util.List;

public record AiWorkerPayload(
        Long lookbookId,
        String styleType,
        String season,
        String targetGender,
        String prompt,
        List<ProductInfo> products
) {
    public record ProductInfo(
            Long productId,
            String category,
            String position,
            String imageUrl
    ) {
    }
}
