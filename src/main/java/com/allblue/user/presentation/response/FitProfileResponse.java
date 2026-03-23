package com.allblue.user.presentation.response;

import com.allblue.user.application.dto.result.FitProfileResult;

public record FitProfileResponse(
        Long id,
        Integer height,
        Integer weight,
        Integer shoulderWidth,
        Integer chestSize,
        Integer waistSize,
        Integer hipSize) {

    public static FitProfileResponse from(FitProfileResult result) {
        return new FitProfileResponse(
                result.id(),
                result.height(),
                result.weight(),
                result.shoulderWidth(),
                result.chestSize(),
                result.waistSize(),
                result.hipSize());
    }
}
