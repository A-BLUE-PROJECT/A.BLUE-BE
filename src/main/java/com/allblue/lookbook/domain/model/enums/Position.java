package com.allblue.lookbook.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Position {
    TOP("상의"),
    BOTTOM("하의"),
    SHOES("신발"),
    OUTER("아우터"),
    ACC("악세서리"),
    HEADWEAR("모자"),
    BAG("가방");

    private final String description;
}
