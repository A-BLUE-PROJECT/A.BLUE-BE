package com.allblue.user.application.command;

import com.allblue.user.domain.model.enums.Gender;

public record UserProfileUpdateCommand(String nickname, Integer height, Integer weight, Gender gender) {}
