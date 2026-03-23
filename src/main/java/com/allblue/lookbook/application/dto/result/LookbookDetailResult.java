package com.allblue.lookbook.application.dto.result;

import com.allblue.lookbook.domain.model.Lookbook;
import com.allblue.lookbook.domain.model.LookbookItem;
import com.allblue.lookbook.domain.model.enums.LookbookStatus;
import com.allblue.lookbook.domain.model.enums.Position;
import com.allblue.lookbook.domain.model.enums.Season;
import com.allblue.lookbook.domain.model.enums.StyleType;
import com.allblue.lookbook.domain.model.enums.TargetGender;
import java.util.List;

public record LookbookDetailResult(
        Long id,
        StyleType styleType,
        Season season,
        TargetGender targetGender,
        String tags,
        LookbookStatus status,
        String originUrl,
        String imageUrl,
        List<LookbookItemResult> items) {

    public record LookbookItemResult(Long productId, Position position) {
        public static LookbookItemResult from(LookbookItem item) {
            return new LookbookItemResult(item.getProductId(), item.getPosition());
        }
    }

    public static LookbookDetailResult from(Lookbook lookbook) {
        String originUrl = lookbook.getLookbookImage() != null
                ? lookbook.getLookbookImage().getOriginUrl()
                : null;
        String imageUrl = lookbook.getLookbookImage() != null
                ? lookbook.getLookbookImage().getImageUrl()
                : null;
        List<LookbookItemResult> items = lookbook.getLookbookItems().stream()
                .map(LookbookItemResult::from)
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
                items);
    }
}
