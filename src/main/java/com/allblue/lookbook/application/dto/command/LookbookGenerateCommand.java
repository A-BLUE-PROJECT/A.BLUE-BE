package com.allblue.lookbook.application.dto.command;

import com.allblue.lookbook.domain.model.Lookbook.LookbookItemInfo;
import com.allblue.lookbook.domain.model.enums.Season;
import com.allblue.lookbook.domain.model.enums.StyleType;
import com.allblue.lookbook.domain.model.enums.TargetGender;
import java.util.List;

public record LookbookGenerateCommand(
        StyleType styleType,
        Season season,
        TargetGender targetGender,
        String tags,
        String modelImageUrl,
        List<LookbookItemInfo> items,
        String prompt) {}
