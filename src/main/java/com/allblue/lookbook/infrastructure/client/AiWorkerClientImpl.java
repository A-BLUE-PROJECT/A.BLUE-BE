package com.allblue.lookbook.infrastructure.client;

import com.allblue.lookbook.application.AiWorkerClient;
import com.allblue.lookbook.application.dto.AiWorkerPayload;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiWorkerClientImpl implements AiWorkerClient {

    private final RestTemplate restTemplate;

    @Value("${worker.n8n.webhook-url}")
    private String webhookUrl;

    @Override
    public void requestGeneration(AiWorkerPayload payload) {
        RequestEntity<AiWorkerPayload> request = RequestEntity
                .post(URI.create(webhookUrl))
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload);

        restTemplate.exchange(request, Void.class);
        log.info("[AiWorker] n8n 트리거 성공 - lookbookId: {}", payload.lookbookId());
    }
}
