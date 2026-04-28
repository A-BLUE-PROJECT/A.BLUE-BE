package com.allblue.lookbook.presentation.response;

import com.allblue.lookbook.application.dto.result.LookbookDetailResult;
import com.allblue.lookbook.domain.model.enums.LookbookStatus;
import com.allblue.lookbook.domain.model.enums.Position;
import com.allblue.lookbook.domain.model.enums.Season;
import com.allblue.lookbook.domain.model.enums.StyleType;
import com.allblue.lookbook.domain.model.enums.TargetGender;
import java.util.List;

public record LookbookDetailResponse(
        Long id,
        StyleType styleType,
        Season season,
        TargetGender targetGender,
        String tags,
        LookbookStatus status,
        String originUrl,
        String imageUrl,
        Integer aiScore,
        List<LookbookItemResponse> items) {

    public record LookbookItemResponse(
            Long productId,
            Position position,
            String brandName,
            String productName,
            Integer price,
            String productImageUrl,
            String originUrl) {}

    public static LookbookDetailResponse from(LookbookDetailResult result) {
        List<LookbookItemResponse> items = result.items().stream()
                .map(item -> new LookbookItemResponse(
                        item.productId(),
                        item.position(),
                        item.brandName(),
                        item.productName(),
                        item.price(),
                        item.productImageUrl(),
                        item.originUrl()))
                .toList();
        return new LookbookDetailResponse(
                result.id(),
                result.styleType(),
                result.season(),
                result.targetGender(),
                result.tags(),
                result.status(),
                result.originUrl(),
                result.imageUrl(),
                result.aiScore(),
                items);
    }
}
