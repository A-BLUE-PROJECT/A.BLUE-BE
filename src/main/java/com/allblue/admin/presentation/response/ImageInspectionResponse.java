package com.allblue.admin.presentation.response;

import com.allblue.admin.application.dto.result.ImageInspectionResult;
import com.allblue.admin.domain.model.InspectionStatus;
import java.time.LocalDateTime;

public record ImageInspectionResponse(
        Long id,
        Long lookbookImageId,
        String imageUrl,
        InspectionStatus status,
        String aiComment,
        Long adminId,
        LocalDateTime createdAt) {
    public static ImageInspectionResponse from(ImageInspectionResult result) {
        return new ImageInspectionResponse(
                result.id(),
                result.lookbookImageId(),
                result.imageUrl(),
                result.status(),
                result.aiComment(),
                result.adminId(),
                result.createdAt());
    }
}
