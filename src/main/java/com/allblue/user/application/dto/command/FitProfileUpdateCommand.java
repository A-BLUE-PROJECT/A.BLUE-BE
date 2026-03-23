package com.allblue.user.application.dto.command;

public record FitProfileUpdateCommand(
        Long userId,
        Integer height,
        Integer weight,
        Integer shoulderWidth,
        Integer chestSize,
        Integer waistSize,
        Integer hipSize) {}
