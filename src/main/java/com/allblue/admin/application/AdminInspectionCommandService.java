package com.allblue.admin.application;

import com.allblue.admin.application.dto.command.InspectionCallbackCommand;
import com.allblue.admin.application.dto.command.InspectionStatusUpdateCommand;
import com.allblue.admin.domain.model.ImageInspection;
import com.allblue.admin.domain.model.InspectionStatus;
import com.allblue.admin.domain.repository.ImageInspectionRepository;
import com.allblue.lookbook.domain.exception.LookbookBusinessException;
import com.allblue.lookbook.domain.exception.LookbookErrorCode;
import com.allblue.lookbook.domain.model.Lookbook;
import com.allblue.lookbook.domain.repository.LookbookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminInspectionCommandService {

    private final ImageInspectionRepository imageInspectionRepository;
    private final LookbookRepository lookbookRepository;

    public void processInspectionCallback(InspectionCallbackCommand command) {
        ImageInspection inspection = imageInspectionRepository.getByLookbookImageId(command.lookbookImageId());
        inspection.updateAiResult(command.status(), command.aiComment());
    }

    public void updateInspectionStatus(InspectionStatusUpdateCommand command) {
        ImageInspection inspection = imageInspectionRepository.getById(command.inspectionId());
        inspection.updateAdminStatus(command.status(), command.adminId());
    }

    @CacheEvict(value = "lookbooks", allEntries = true)
    public void approveByLookbookId(Long lookbookId, Long adminId) {
        ImageInspection inspection = imageInspectionRepository.getByLookbookId(lookbookId);
        inspection.updateAdminStatus(InspectionStatus.ADMIN_APPROVED, adminId);

        Lookbook lookbook = lookbookRepository.findById(lookbookId)
                .orElseThrow(() -> new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_NOT_FOUND));
        lookbook.approve();
    }

    @CacheEvict(value = "lookbooks", allEntries = true)
    public void rejectByLookbookId(Long lookbookId, Long adminId) {
        ImageInspection inspection = imageInspectionRepository.getByLookbookId(lookbookId);
        inspection.updateAdminStatus(InspectionStatus.ADMIN_REJECTED, adminId);

        Lookbook lookbook = lookbookRepository.findById(lookbookId)
                .orElseThrow(() -> new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_NOT_FOUND));
        lookbook.reject();
    }
}
