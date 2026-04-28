package com.allblue.category.presentation.request;

import com.allblue.category.application.dto.command.CreateCategoryCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(
        @NotBlank(message = "카테고리 이름은 필수입니다.") @Size(max = 10, message = "카테고리 이름은 10자 이하여야 합니다.")
        String name) {
    public CreateCategoryCommand toCommand() {
        return new CreateCategoryCommand(name);
    }
}