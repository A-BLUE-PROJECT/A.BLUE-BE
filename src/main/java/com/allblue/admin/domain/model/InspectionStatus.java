package com.allblue.admin.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InspectionStatus {
    PENDING("검수 대기"),
    AI_PASSED("AI 통과"),
    AI_FLAGGED("의심 (관리자 검토)"),
    WORKER_ERROR("AI 워커 통신 실패"),
    ADMIN_APPROVED("관리자 최종 승인"),
    ADMIN_REJECTED("관리자 최종 거절");

    private final String description;

    public boolean isAdminResult() {
        return this == ADMIN_APPROVED || this == ADMIN_REJECTED;
    }

    public boolean isProcessableByAdmin() {
        return this == AI_PASSED || this == AI_FLAGGED;
    }

    public boolean isAiResult() {
        return this == AI_PASSED || this == AI_FLAGGED;
    }

    public boolean isProcessableByAi() {
        return this == PENDING || this == WORKER_ERROR;
    }
}
