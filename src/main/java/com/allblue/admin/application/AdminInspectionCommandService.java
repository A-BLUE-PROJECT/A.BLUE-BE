package com.allblue.admin.application;

import com.allblue.admin.application.dto.command.InspectionCallbackCommand;
import com.allblue.admin.application.dto.command.InspectionStatusUpdateCommand;
import com.allblue.admin.domain.model.ImageInspection;
import com.allblue.admin.domain.repository.ImageInspectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminInspectionCommandService {

    private final ImageInspectionRepository imageInspectionRepository;

    @Transactional
    public void processInspectionCallback(InspectionCallbackCommand command) {
        ImageInspection inspection = imageInspectionRepository.getByLookbookImageId(command.lookbookImageId());
        inspection.updateAiResult(command.status(), command.aiComment());
    }

    @Transactional
    public void updateInspectionStatus(InspectionStatusUpdateCommand command) {
        ImageInspection inspection = imageInspectionRepository.getById(command.inspectionId());
        inspection.updateAdminStatus(command.status(), command.adminId());
    }
}
