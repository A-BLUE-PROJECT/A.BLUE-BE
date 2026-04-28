package com.allblue.product.presentation.request;

import jakarta.validation.constraints.NotNull;

public record ProductHiddenRequest(@NotNull Boolean hidden) {}
