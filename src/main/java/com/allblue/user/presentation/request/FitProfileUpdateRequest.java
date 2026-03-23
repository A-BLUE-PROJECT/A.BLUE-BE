package com.allblue.user.presentation.request;

import com.allblue.user.application.dto.command.FitProfileUpdateCommand;

public record FitProfileUpdateRequest(
        Integer height,
        Integer weight,
        Integer shoulderWidth,
        Integer chestSize,
        Integer waistSize,
        Integer hipSize) {

    public FitProfileUpdateCommand toCommand(Long userId) {
        return new FitProfileUpdateCommand(userId, height, weight, shoulderWidth, chestSize, waistSize, hipSize);
    }
}
