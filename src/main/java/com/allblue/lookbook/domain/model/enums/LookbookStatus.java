package com.allblue.lookbook.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LookbookStatus {
    PENDING("생성 대기"),
    COMPLETED("생성 완료"),
    FAILED("생성 실패");

    private final String description;
}
