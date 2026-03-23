package com.allblue.admin.presentation.request;

import com.allblue.admin.application.dto.command.InspectionCallbackCommand;
import com.allblue.admin.domain.model.InspectionStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InspectionCallbackRequest(
        @NotNull(message = "룩북 이미지 ID는 필수입니다.") Long lookbookImageId,

        @NotNull(message = "?�태값�? ?�수?�니??") InspectionStatus status,

        @Size(max = 500, message = "코멘?�는 500?��? 초과?????�습?�다.") String comment) {
    public InspectionCallbackCommand toCommand() {
        return new InspectionCallbackCommand(lookbookImageId, status, comment);
    }
}
