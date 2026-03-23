package com.allblue.lookbook.application.dto.query;

import com.allblue.lookbook.domain.model.enums.Season;
import com.allblue.lookbook.domain.model.enums.StyleType;
import com.allblue.lookbook.domain.model.enums.TargetGender;

public record LookbookSearchQuery(
        StyleType styleType,
        Season season,
        TargetGender targetGender,
        Long cursorId,
        int size) {}
