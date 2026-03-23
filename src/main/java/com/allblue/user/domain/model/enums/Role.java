package com.allblue.user.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    MEMBER("ROLE_MEMBER", "일반 회원"),
    SELLER("ROLE_SELLER", "셀러"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String title;
}