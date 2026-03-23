package com.allblue.user.application.command;

import com.allblue.user.domain.model.enums.Gender;

public record UserOnboardingCommand(String nickname, Integer height, Integer weight, Gender gender) {}
