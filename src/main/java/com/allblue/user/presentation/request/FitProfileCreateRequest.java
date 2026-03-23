package com.allblue.user.presentation.request;

import com.allblue.user.application.dto.command.FitProfileCreateCommand;

public record FitProfileCreateRequest(
        Integer height,
        Integer weight,
        Integer shoulderWidth,
        Integer chestSize,
        Integer waistSize,
        Integer hipSize) {

    public FitProfileCreateCommand toCommand(Long userId) {
        return new FitProfileCreateCommand(userId, height, weight, shoulderWidth, chestSize, waistSize, hipSize);
    }
}
