package com.allblue.lookbook.application.port.out;

public interface AiWorkerClient {
    /**
     * AI 워커(n8n 등)에게 룩북 생성 작업을 비동기로 요청합니다.
     */
    void requestGeneration(AiWorkerPayload payload);
}
