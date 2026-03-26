package com.allblue.lookbook.application.dto.result;

import com.allblue.lookbook.domain.model.Lookbook;
import com.allblue.lookbook.domain.model.LookbookItem;
import com.allblue.lookbook.domain.model.enums.LookbookStatus;
import com.allblue.lookbook.domain.model.enums.Position;
import com.allblue.lookbook.domain.model.enums.Season;
import com.allblue.lookbook.domain.model.enums.StyleType;
import com.allblue.lookbook.domain.model.enums.TargetGender;
import com.allblue.product.domain.model.Product;
import java.util.List;
import java.util.Map;

public record LookbookDetailResult(
        Long id,
        StyleType styleType,
        Season season,
        TargetGender targetGender,
        String tags,
        LookbookStatus status,
        String originUrl,
        String imageUrl,
        Integer aiScore,
        List<LookbookItemResult> items) {

    public record LookbookItemResult(
            Long productId,
            Position position,
            String brandName,
            String productName,
            Integer price,
            String productImageUrl,
            String originUrl) {}

    public static LookbookDetailResult from(Lookbook lookbook, Map<Long, Product> productMap) {
        String originUrl = lookbook.getLookbookImage() != null
                ? lookbook.getLookbookImage().getOriginUrl()
                : null;
        String imageUrl = lookbook.getLookbookImage() != null
                ? lookbook.getLookbookImage().getImageUrl()
                : null;

        List<LookbookItemResult> items = lookbook.getLookbookItems().stream()
                .map(item -> toItemResult(item, productMap.get(item.getProductId())))
                .toList();

        return new LookbookDetailResult(
                lookbook.getId(),
                lookbook.getStyleType(),
                lookbook.getSeason(),
                lookbook.getTargetGender(),
                lookbook.getTags(),
                lookbook.getStatus(),
                originUrl,
                imageUrl,
                lookbook.getAiScore(),
                items);
    }

    private static LookbookItemResult toItemResult(LookbookItem item, Product product) {
        return new LookbookItemResult(
                item.getProductId(),
                item.getPosition(),
                product != null ? product.getBrandName() : null,
                product != null ? product.getProductName() : null,
                product != null ? product.getPrice() : null,
                product != null ? product.getProductImageUrl() : null,
                product != null ? product.getOriginUrl() : null);
    }
}
