package com.allblue.common.worker;

public interface InspectionFallbackHandler {
    void handleFailure(Long targetId);
}
