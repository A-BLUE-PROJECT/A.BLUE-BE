package com.allblue.admin.application;

import com.allblue.admin.domain.model.InspectionStatus;
import com.allblue.admin.domain.repository.ImageInspectionRepository;
import com.allblue.common.worker.InspectionFallbackHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminInspectionFallbackServiceImpl implements InspectionFallbackHandler {

    private final ImageInspectionRepository inspectionRepository;

    @Override
    @Transactional
    public void handleFailure(Long lookbookImageId) {
        log.error("[Admin Fallback] n8n 워커 수신 실패. Admin 이메일에서 예외 처리 시작 - lookbookImageId: {}", lookbookImageId);

        try {
            inspectionRepository
                    .findByLookbookImageId(lookbookImageId)
                    .ifPresentOrElse(
                            inspection -> {
                                inspection.updateAiResult(InspectionStatus.WORKER_ERROR, "AI 서버 수신 실패로 검수 지연");
                                log.info("[Admin Fallback] 검수 상태 WORKER_ERROR로 업데이트 완료 - lookbookImageId: {}", lookbookImageId);
                            },
                            () -> log.warn(
                                    "[Admin Fallback] 검수 정보를 찾을 수 없습니다. 상태 업데이트 실패 - lookbookImageId: {}", lookbookImageId));
        } catch (Exception e) {
            log.error("[Admin Fallback] 검수 상태 업데이트 실패 - lookbookImageId: {}, Reason: {}", lookbookImageId, e.getMessage(), e);
        }
    }
}
