package com.allblue.lookbook.presentation.response;

import com.allblue.lookbook.application.dto.result.LookbookResult;
import com.allblue.lookbook.domain.model.enums.LookbookStatus;
import com.allblue.lookbook.domain.model.enums.Season;
import com.allblue.lookbook.domain.model.enums.StyleType;
import com.allblue.lookbook.domain.model.enums.TargetGender;
import java.time.LocalDateTime;

public record LookbookResponse(
        Long id,
        StyleType styleType,
        Season season,
        TargetGender targetGender,
        String tags,
        LookbookStatus status,
        String imageUrl,
        Integer aiScore,
        LocalDateTime createdAt) {

    public static LookbookResponse from(LookbookResult result) {
        return new LookbookResponse(
                result.id(),
                result.styleType(),
                result.season(),
                result.targetGender(),
                result.tags(),
                result.status(),
                result.imageUrl(),
                result.aiScore(),
                result.createdAt());
    }
}
