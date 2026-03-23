package com.allblue.user.application.dto.command;

public record FitProfileCreateCommand(
        Long userId,
        Integer height,
        Integer weight,
        Integer shoulderWidth,
        Integer chestSize,
        Integer waistSize,
        Integer hipSize) {}
