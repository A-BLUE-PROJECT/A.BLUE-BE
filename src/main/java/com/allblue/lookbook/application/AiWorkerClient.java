package com.allblue.lookbook.application;

import com.allblue.lookbook.application.dto.AiWorkerPayload;

public interface AiWorkerClient {
    void requestGeneration(AiWorkerPayload payload);
}
