package com.allblue.category.presentation.request;

import com.allblue.category.application.dto.command.UpdateCategoryNameCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCategoryNameRequest(
        @NotBlank(message = "카테고리 이름은 필수입니다.") @Size(max = 10, message = "카테고리 이름은 10자 이하여야 합니다.")
        String name) {
    public UpdateCategoryNameCommand toCommand() {
        return new UpdateCategoryNameCommand(name);
    }
}