package com.allblue.activelog.presentation.request;

import com.allblue.activelog.domain.model.SwipeType;
import jakarta.validation.constraints.NotNull;

public record SwipeRequest(
        @NotNull(message = "스와이프 타입(LIKE/DISLIKE)은 필수입니다.") SwipeType swipeType) {}