package com.allblue.lookbook.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StyleType {
    CASUAL("캐주얼"),
    FORMAL("포멀"),
    STREET("스트릿"),
    SPORTY("스포티");

    private final String description;
}
