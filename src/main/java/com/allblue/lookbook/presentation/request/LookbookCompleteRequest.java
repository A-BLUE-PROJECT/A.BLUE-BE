package com.allblue.lookbook.presentation.request;

import com.allblue.lookbook.application.dto.command.LookbookCompleteCommand;
import jakarta.validation.constraints.NotBlank;

public record LookbookCompleteRequest(
        @NotBlank String originUrl,
        @NotBlank String imageUrl,
        Integer aiScore) {

    public LookbookCompleteCommand toCommand(Long lookbookId) {
        return new LookbookCompleteCommand(lookbookId, originUrl, imageUrl, aiScore);
    }
}
