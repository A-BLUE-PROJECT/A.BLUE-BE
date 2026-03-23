package com.allblue.admin.presentation.request;

import com.allblue.admin.application.dto.command.InspectionStatusUpdateCommand;
import com.allblue.admin.domain.model.InspectionStatus;
import jakarta.validation.constraints.NotNull;

public record InspectionStatusUpdateRequest(
        @NotNull(message = "蹂寃쏀 ??媛? ??????") InspectionStatus status) {
    public InspectionStatusUpdateCommand toCommand(Long inspectionId, Long adminId) {
        return new InspectionStatusUpdateCommand(inspectionId, status, adminId);
    }
}
