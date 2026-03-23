package com.allblue.user.application.dto.result;

import com.allblue.user.domain.model.FitProfile;

public record FitProfileResult(
        Long id,
        Integer height,
        Integer weight,
        Integer shoulderWidth,
        Integer chestSize,
        Integer waistSize,
        Integer hipSize) {

    public static FitProfileResult from(FitProfile fitProfile) {
        return new FitProfileResult(
                fitProfile.getId(),
                fitProfile.getHeight(),
                fitProfile.getWeight(),
                fitProfile.getShoulderWidth(),
                fitProfile.getChestSize(),
                fitProfile.getWaistSize(),
                fitProfile.getHipSize());
    }
}
