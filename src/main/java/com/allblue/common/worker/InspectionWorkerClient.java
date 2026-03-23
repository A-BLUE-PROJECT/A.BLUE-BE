package com.allblue.common.worker;

public interface InspectionWorkerClient {
    void sendInspectionRequest(Long cardImageId, String originUrl, String imageUrl);
}
