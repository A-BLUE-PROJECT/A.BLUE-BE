package com.allblue.lookbook.application;

import com.allblue.lookbook.application.port.out.AiWorkerClient;
import com.allblue.lookbook.application.port.out.AiWorkerPayload;
import com.allblue.lookbook.domain.exception.LookbookBusinessException;
import com.allblue.lookbook.domain.exception.LookbookErrorCode;
import com.allblue.lookbook.domain.model.Lookbook;
import com.allblue.lookbook.domain.model.enums.LookbookStatus;
import com.allblue.lookbook.domain.repository.LookbookRepository;
import com.allblue.product.domain.model.Product;
import com.allblue.product.domain.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LookbookAiPipelineService {

    private final LookbookRepository lookbookRepository;
    private final ProductRepository productRepository;
    private final AiWorkerClient aiWorkerClient;

    /**
     * 1. 룩북 생성 요청 (PENDING 상태의 룩북을 AI 워커로 전송)
     */
    @Transactional(readOnly = true)
    public void triggerGeneration(Long lookbookId) {
        Lookbook lookbook = lookbookRepository.findById(lookbookId)
                .orElseThrow(() -> new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_NOT_FOUND));

        if (lookbook.getStatus() != LookbookStatus.PENDING) {
            log.warn("Lookbook {} is not in PENDING state. Current state: {}", lookbookId, lookbook.getStatus());
            return;
        }

        // 1. 룩북에 포함된 상품들의 실제 정보를 조회
        List<AiWorkerPayload.ProductInfo> productInfos = lookbook.getLookbookItems().stream()
                .map(item -> {
                    // TODO: Product 도메인이 독립되어 있으므로 Fetch 제어가 필요함. MVP 단계에선 단순 조회.
                    Product product = productRepository.findById(item.getProductId())
                            .orElseThrow(() -> new LookbookBusinessException(LookbookErrorCode.PRODUCT_NOT_FOUND));
                    
                    return new AiWorkerPayload.ProductInfo(
                            product.getId(),
                            product.getMappedCategory() != null ? product.getMappedCategory().name() : "UNKNOWN",
                            item.getPosition().name(),
                            product.getProductImageUrl()
                    );
                })
                .toList();

        // 2. AI 워커에게 전송할 페이로드 구성
        AiWorkerPayload payload = new AiWorkerPayload(
                lookbook.getId(),
                lookbook.getStyleType().name(),
                lookbook.getSeason().name(),
                lookbook.getTargetGender() != null ? lookbook.getTargetGender().name() : null,
                null,
                productInfos
        );

        // 3. 비동기 워커 호출 (의존성 역전)
        log.info("Triggering AI generation for Lookbook {}", lookbookId);
        aiWorkerClient.requestGeneration(payload);
    }

    /**
     * 2. PENDING 상태로 오래 머물러 있는 룩북 재시도 (스케줄러에서 호출)
     */
    @Transactional(readOnly = true)
    public void retryPendingLookbooks(int minutesThreshold) {
        LocalDateTime thresholdTime = LocalDateTime.now().minusMinutes(minutesThreshold);
        List<Lookbook> pendingLookbooks = lookbookRepository.findByStatusAndCreatedAtBefore(LookbookStatus.PENDING, thresholdTime);
        
        log.info("Found {} pending lookbooks older than {} minutes. Retrying...", pendingLookbooks.size(), minutesThreshold);
        
        for (Lookbook lookbook : pendingLookbooks) {
            try {
                triggerGeneration(lookbook.getId());
            } catch (Exception e) {
                log.error("Failed to retry Lookbook {}", lookbook.getId(), e);
            }
        }
    }
}
