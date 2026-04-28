package com.allblue.user.application.dto.command;

import com.allblue.user.domain.model.enums.Provider;

public record UserCreateCommand(String email, Provider provider, String providerId) {}
