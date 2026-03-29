package com.allblue.lookbook.application;

import com.allblue.lookbook.application.AiWorkerClient;
import com.allblue.lookbook.application.dto.AiWorkerPayload;
import com.allblue.lookbook.domain.exception.LookbookBusinessException;
import com.allblue.lookbook.domain.exception.LookbookErrorCode;
import com.allblue.lookbook.domain.model.Lookbook;
import com.allblue.lookbook.domain.model.enums.LookbookStatus;
import com.allblue.lookbook.domain.repository.LookbookRepository;
import com.allblue.product.domain.model.Product;
import com.allblue.product.domain.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LookbookAiPipelineService {

    private static final int PENDING_THRESHOLD_MINUTES = 10;
    private static final int MAX_RETRY_COUNT = 3;

    private final LookbookRepository lookbookRepository;
    private final ProductRepository productRepository;
    private final AiWorkerClient aiWorkerClient;

    /**
     * PENDING 상태의 룩북을 AI 워커로 전송 (직접 호출 또는 재시도 스케줄러에서 사용)
     */
    @Transactional(readOnly = true)
    public void triggerGeneration(Long lookbookId) {
        Lookbook lookbook = lookbookRepository.findById(lookbookId)
                .orElseThrow(() -> new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_NOT_FOUND));

        if (lookbook.getStatus() != LookbookStatus.PENDING) {
            log.warn("Lookbook {} is not in PENDING state. Current state: {}", lookbookId, lookbook.getStatus());
            return;
        }

        List<Long> productIds = lookbook.getLookbookItems().stream()
                .map(item -> item.getProductId())
                .toList();

        Map<Long, Product> productMap = productRepository.findAllByIds(productIds).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        List<AiWorkerPayload.ProductInfo> productInfos = lookbook.getLookbookItems().stream()
                .map(item -> {
                    Product product = productMap.get(item.getProductId());
                    return new AiWorkerPayload.ProductInfo(
                            item.getProductId(),
                            product != null ? product.getMappedCategory().name() : null,
                            item.getPosition().name(),
                            product != null ? product.getProductImageUrl() : null);
                })
                .toList();

        AiWorkerPayload payload = new AiWorkerPayload(
                lookbook.getId(),
                lookbook.getStyleType().name(),
                lookbook.getSeason().name(),
                lookbook.getTargetGender() != null ? lookbook.getTargetGender().name() : null,
                null,
                productInfos);

        log.info("Triggering AI generation for Lookbook {}", lookbookId);
        aiWorkerClient.requestGeneration(payload);
    }

    /**
     * PENDING 상태로 10분 이상 머물러 있는 룩북 재시도 (최대 3회, 초과 시 FAILED 처리)
     */
    @Scheduled(fixedDelay = 600_000)
    @Transactional
    public void retryPendingLookbooks() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(PENDING_THRESHOLD_MINUTES);
        List<Lookbook> pendingLookbooks = lookbookRepository.findByStatusAndCreatedAtBefore(LookbookStatus.PENDING, threshold);

        log.info("Found {} pending lookbooks older than {} minutes. Retrying...", pendingLookbooks.size(), PENDING_THRESHOLD_MINUTES);

        for (Lookbook lookbook : pendingLookbooks) {
            boolean canRetry = lookbook.incrementRetryOrFail();
            if (!canRetry) {
                log.warn("Lookbook {} exceeded max retry count ({}). Marked as FAILED.", lookbook.getId(), MAX_RETRY_COUNT);
                lookbookRepository.save(lookbook);
                continue;
            }
            lookbookRepository.save(lookbook);
            try {
                triggerGeneration(lookbook.getId());
            } catch (Exception e) {
                log.error("Failed to retry Lookbook {}", lookbook.getId(), e);
            }
        }
    }
}
