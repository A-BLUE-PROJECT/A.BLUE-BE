package com.allblue.common.worker;

import com.allblue.common.worker.dto.CardImageInspectionPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class InspectionWorkerClientImpl implements InspectionWorkerClient {

    private static final long RATE_LIMIT_DELAY_MS = 15_000;

    private final RestTemplate restTemplate;
    private final InspectionFallbackHandler fallbackHandler;

    @Value("${worker.n8n.webhook-url}")
    private String webhookUrl;

    @Async("inspectionTaskExecutor")
    @Override
    public void sendInspectionRequest(Long cardImageId, String originUrl, String imageUrl) {
        log.info("[Inspection Worker] n8n 요청을 시작합니다 - cardImageId: {}", cardImageId);
        CardImageInspectionPayload payload = new CardImageInspectionPayload(cardImageId, originUrl, imageUrl);

        try {
            Thread.sleep(RATE_LIMIT_DELAY_MS);
            restTemplate.postForEntity(webhookUrl, payload, Void.class);
            log.info("[Inspection Worker] n8n 요청이 완료되었습니다 - cardImageId: {}", cardImageId);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            log.warn("[Inspection Worker] 요청이 인터럽트로 중단됩니다 - cardImageId: {}", cardImageId);
            fallbackHandler.handleFailure(cardImageId);
        } catch (Exception e) {
            log.error("[Inspection Worker] n8n 오류가 발생했습니다 - cardImageId: {}, Reason: {}", cardImageId, e.getMessage(), e);
            fallbackHandler.handleFailure(cardImageId);
        }
    }
}
