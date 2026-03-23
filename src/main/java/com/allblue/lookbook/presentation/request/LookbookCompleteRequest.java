package com.allblue.lookbook.presentation.request;

import com.allblue.lookbook.application.dto.command.LookbookCompleteCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LookbookCompleteRequest(
        @NotBlank String originUrl,
        @NotBlank String imageUrl) {

    public LookbookCompleteCommand toCommand(@NotNull Long lookbookId) {
        return new LookbookCompleteCommand(lookbookId, originUrl, imageUrl);
    }
}
