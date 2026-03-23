package com.allblue.lookbook.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TargetGender {
    WOMEN("여성"),
    MEN("남성"),
    UNDEFINED("미정");

    private final String description;
}
