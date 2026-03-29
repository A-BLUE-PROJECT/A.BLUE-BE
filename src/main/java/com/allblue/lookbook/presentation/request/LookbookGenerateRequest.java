package com.allblue.lookbook.presentation.request;

import com.allblue.lookbook.application.dto.command.LookbookGenerateCommand;
import com.allblue.lookbook.domain.model.Lookbook.LookbookItemInfo;
import com.allblue.lookbook.domain.model.enums.Position;
import com.allblue.lookbook.domain.model.enums.Season;
import com.allblue.lookbook.domain.model.enums.StyleType;
import com.allblue.lookbook.domain.model.enums.TargetGender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record LookbookGenerateRequest(
        @NotNull StyleType styleType,
        @NotNull Season season,
        TargetGender targetGender,
        String tags,
        String modelImageUrl,
        @NotEmpty @Valid List<LookbookItemRequest> items,
        String prompt) {

    public record LookbookItemRequest(
            @NotNull Long productId,
            @NotNull Position position) {}

    public LookbookGenerateCommand toCommand() {
        List<LookbookItemInfo> itemInfos = items.stream()
                .map(i -> new LookbookItemInfo(i.productId(), i.position()))
                .toList();
        return new LookbookGenerateCommand(styleType, season, targetGender, tags, modelImageUrl, itemInfos, prompt);
    }
}
