package com.allblue.lookbook.application.dto.result;

import com.allblue.lookbook.domain.model.Lookbook;
import com.allblue.lookbook.domain.model.enums.LookbookStatus;
import com.allblue.lookbook.domain.model.enums.Season;
import com.allblue.lookbook.domain.model.enums.StyleType;
import com.allblue.lookbook.domain.model.enums.TargetGender;

public record LookbookResult(
        Long id,
        StyleType styleType,
        Season season,
        TargetGender targetGender,
        String tags,
        LookbookStatus status,
        String imageUrl,
        Integer aiScore) {

    public static LookbookResult from(Lookbook lookbook) {
        String imageUrl = lookbook.getLookbookImage() != null
                ? lookbook.getLookbookImage().getImageUrl()
                : null;
        return new LookbookResult(
                lookbook.getId(),
                lookbook.getStyleType(),
                lookbook.getSeason(),
                lookbook.getTargetGender(),
                lookbook.getTags(),
                lookbook.getStatus(),
                imageUrl,
                lookbook.getAiScore());
    }
}
